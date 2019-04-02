package org.jboss.resteasy.test.war;

import java.io.ByteArrayInputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.resteasy.spi.HttpResponseCodes;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;


/**
 * @author <a href="mailto:bill@burkecentral.com">Stephane Epardaud</a>
 * @version $Revision: 1 $
 */
public class JSAPITest
{
   static final String JSAPIURL = "http://localhost:9095/rest-js";


   @Test
   public void test() throws Exception
   {
      HttpClient client = new HttpClient();
      GetMethod method = new GetMethod(JSAPIURL);
      int status = client.executeMethod(method);
      Assert.assertEquals(HttpResponseCodes.SC_OK, status);
      String response = method.getResponseBodyAsString();
      Assert.assertTrue("javascript response is expected", response.contains("MyResource.getFoo"));
      method.releaseConnection();
   }

   @Test
   public void testService() throws Exception
   {
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:9095/rest/mine/pairs");
      Invocation.Builder request = target.request();
      request.accept("application/json");
      String rtn = request.get().readEntity(String.class);
      String expected = "{\"key\":\"key22\",\"value\":\"value22\"}";
      Assert.assertTrue("Expected json response", rtn.contains(expected));

      target = client.target("http://localhost:9095/rest/mine/pairs");
      request = target.request();
      request.accept("application/xml");
      rtn = request.get().readEntity(String.class);
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(rtn.getBytes()));
      Assert.assertTrue("Expected xml element",doc.getDocumentElement().getElementsByTagName("pair").getLength() == 23);
   }

}
