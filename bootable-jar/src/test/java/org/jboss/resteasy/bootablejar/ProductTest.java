package org.jboss.resteasy.bootablejar;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
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
