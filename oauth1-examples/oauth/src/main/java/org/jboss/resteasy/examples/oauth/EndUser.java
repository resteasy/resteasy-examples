package org.jboss.resteasy.examples.oauth;

import org.jboss.resteasy.auth.oauth.OAuthUtils;
import org.jboss.resteasy.util.Base64;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.xml.sax.InputSource;

import javax.swing.JOptionPane;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


public class EndUser
{
       
   private static final String ConsumerWebAppURL;
   private static final String EndUserResourceURL;
   
   static {
       Properties props = new Properties();
       try {
           props.load(EndUser.class.getResourceAsStream("/oauth.properties"));
       } catch (Exception ex) {
           throw new RuntimeException("oauth.properties resource is not available");
       }
       ConsumerWebAppURL = props.getProperty("consumer.webapp.url");
       EndUserResourceURL = props.getProperty("enduser.resource.url");
   }
   

   
   public static void main(String [] args) throws Exception {
       EndUser user = new EndUser();
       
       // Step1 : request a service from a 3rd party service
       String authorizationURI = user.requestServiceFromThirdPartyWebApp();
       // Step2 : authorize a consumer's temporarily request token
       String callback = user.authorizeConsumerRequestToken(authorizationURI);
       // Step3 : return an authorized request token to the consumer using the provided callback URI
       String endUserResourceData = user.setCallback(callback);
       System.out.println("Success : " + endUserResourceData);
       
       // confirm the end user can access its own resources directly
       if (!"true".equals(System.getProperty("jetty")))
       {
           accessEndUserResource("/resource1");
           accessEndUserResource("/resource2");
           accessEndUserResource("/invisible");
       }
   }
   
   private static void accessEndUserResource(String relativeURI) throws Exception
   {
      WebTarget target = ClientBuilder.newClient().target(EndUserResourceURL + relativeURI);
      String base64Credentials = new String(Base64.encodeBytes("admin:admin".getBytes()));
      target.request().header("Authorization", "Basic " + base64Credentials);
      Response response = null;
      try
      {
         response = target.request().get();
         if ("/invisible".equals(relativeURI)) {
            if (response.getStatus() != 401) {
               throw new RuntimeException("End user can access the invisible resource");
            } else {
               return;
            }
         }
         if (HttpResponseCodes.SC_OK != response.getStatus()) {
            throw new RuntimeException("End user can not access its own resources");
        }
        System.out.println("End user resource : " + response.getEntity());
      }
      finally
      {
         response.close();
      }
   }
   
   /**
    * End user requests that a well-known 3rd party web application
    * does something useful on its behalf
    * @return authorizationURI where the end user has been redirected for
    *         the consumer temporarily request token be authorized
    * @throws Exception
    */
   public String requestServiceFromThirdPartyWebApp() throws Exception
   {
      String url = ConsumerWebAppURL + "?scope=" + OAuthUtils.encodeForOAuth(EndUserResourceURL);
      WebTarget target = ClientBuilder.newClient().target(url);
      Invocation.Builder builder = target.request();
      //TODO:look at how can we support pre 2.0's request.followRedirects(false);
      Response response = null;
      try
      {
         response = builder.get();
         if (302 != response.getStatus()) {
            // note that if the end user knows about the OAuth service's token authorization endpoint
            // then it can try to read the request token from the body and build the authorization URL
            // itself, in case the consumer has not provided the redirection URI
            throw new RuntimeException("Service request has failed - redirection is expected");
        }
        String authorizationURI = response.getStringHeaders().getFirst("Location");
        if (authorizationURI == null) {
            throw new RuntimeException("Token authorization URI is missing");
        }
        return authorizationURI;
      }
      finally
      {
         response.close();
      }
   }
   
   /**
    * End user follows the redirection by going to the authorizationURI 
    * provided by a 3rd party consumer
    * @param url the token authorization URI
    * @return the 3rd party callback URI where the authorization verifier needs to posted to
    * @throws Exception
    */
   public String authorizeConsumerRequestToken(String url) throws Exception
   {
      WebTarget target = ClientBuilder.newClient().target(url);
      // request that XML formatted authorization request is presented
      Invocation.Builder builder = target.request();
      builder.header("Accept", "application/xml");
      String base64Credentials = new String(Base64.encodeBytes("admin:admin".getBytes()));
      builder.header("Authorization", "Basic " + base64Credentials);
      Response response = null;
      try
      {
         response = builder.get();
         if (200 != response.getStatus()) {
            throw new RuntimeException("No authorization request data is available");
         }
         // at this stage we have an XML-formatted token authorization request
         String body = response.readEntity(String.class);
         String consumerId = evaluateBody(new ByteArrayInputStream(body.getBytes()),
                                          "/ns:tokenAuthorizationRequest/ns:consumerId/text()");
         String consumerName = evaluateBody(new ByteArrayInputStream(body.getBytes()),
                                          "/ns:tokenAuthorizationRequest/ns:consumerName/text()");
         String requestScope = evaluateBody(new ByteArrayInputStream(body.getBytes()),
                                          "/ns:tokenAuthorizationRequest/ns:scopes/text()");
         String requestPermission = evaluateBody(new ByteArrayInputStream(body.getBytes()),
                                          "/ns:tokenAuthorizationRequest/ns:permissions/text()");
         // TODO : ask about read/write permissions
         String message = "Authorize " 
                          + ("".equals(consumerName) ? consumerId : consumerName)
                          + System.getProperty("line.separator")
                          + " to access " + ("".equals(requestScope) ? "your resources" : requestScope)
                          + (requestPermission == null ? "" :
                              (System.getProperty("line.separator")
                               + " and grant the following permissions : \"" + requestPermission + "\""))
                          + " (yes/no) ?";
         String decision = JOptionPane.showInputDialog(message);
         if (decision == null || !"yes".equalsIgnoreCase(decision)) {
             decision = "no";
         }
         String replyTo = evaluateBody(new ByteArrayInputStream(body.getBytes()),
                                          "/ns:tokenAuthorizationRequest/@replyTo");
         replyTo += "&xoauth_end_user_decision=" + decision.toLowerCase();
         return confirmAuthorization(replyTo);
      }
      finally
      {
         response.close();
      }
   }
   
   private String evaluateBody(InputStream body, String expression) {
       XPath xpath = XPathFactory.newInstance().newXPath();
       xpath.setNamespaceContext(new NamespaceContextImpl(
               Collections.singletonMap("ns", "http://org.jboss.com/resteasy/oauth")));
       try {
           Object result = (Object)xpath.evaluate(expression, new InputSource(body), XPathConstants.STRING);
           return (String)result;
       } catch (XPathExpressionException ex) {
           throw new IllegalArgumentException("Illegal XPath expression '" + expression + "'", ex);
       }

   }
   
   public String confirmAuthorization(String url) throws Exception
   {
      WebTarget target = ClientBuilder.newClient().target(url);
      String base64Credentials = new String(Base64.encodeBytes("admin:admin".getBytes()));
      Invocation.Builder builder = target.request();
      builder.header("Authorization", "Basic " + base64Credentials);
      Response response = null;
      try {
         response = builder.post(null);
         if (302 != response.getStatus()) {
            throw new RuntimeException("Initiation failed");
         }
         // check that we got all tokens
         String callbackURI = response.getStringHeaders().getFirst("Location");
         if (callbackURI == null) {
             throw new RuntimeException("Callback failed");
         }
         return callbackURI;
      }
      finally {
         response.close();
      }
   }
   
   public String setCallback(String url) throws Exception
   {
      WebTarget target = ClientBuilder.newClient().target(url);
      Response response = target.request().post(null);
      if (200 != response.getStatus()) {
         response.close();
         throw new RuntimeException("Service failed");
      }
      return response.readEntity(String.class);
   }
   
   private static class NamespaceContextImpl implements NamespaceContext {
       
       private Map<String, String> namespaces;
       
       public NamespaceContextImpl(Map<String, String> namespaces) {
           this.namespaces = namespaces;    
       }

       public String getNamespaceURI(String prefix) {
           return namespaces.get(prefix);
       }

       public String getPrefix(String namespace) {
           for (Map.Entry<String, String> entry : namespaces.entrySet()) {
               if (entry.getValue().equals(namespace)) {
                   return entry.getKey();
               }
           }
           return null;
       }

       public Iterator getPrefixes(String namespace) {
           String prefix = namespaces.get(namespace);
           if (prefix == null) {
               return null;
           }
           return Collections.singletonList(prefix).iterator();
       }
   }

}
