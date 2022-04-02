package org.jboss.resteasy.test.war;

import java.io.ByteArrayInputStream;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
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
      try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
         HttpGet method = new HttpGet(JSAPIURL);
         CloseableHttpResponse response = client.execute(method);
         Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
         Assert.assertTrue("javascript response is expected", EntityUtils.toString(response.getEntity()).contains("MyResource.getFoo"));
         method.releaseConnection();
      }
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
