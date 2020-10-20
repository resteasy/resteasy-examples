package org.jboss.resteasy.bootablejar;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Test;

public class ProductTest
{
   @Test
   public void testResponse() throws Exception
   {
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:8080/products");
      Response response = target.request().get();
      try
      {
         Assert.assertEquals(200, response.getStatus());
         Assert.assertTrue("Unexpected json result", response.readEntity(String.class).contains("RHEL"));
      }
      finally
      {
         response.close();
         client.close();
      }
   }
}
