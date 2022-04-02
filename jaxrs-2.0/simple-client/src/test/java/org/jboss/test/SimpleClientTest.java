package org.jboss.test;

import junit.framework.Assert;
import org.jboss.example.jaxrs2.async.Customer;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.junit.Test;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class SimpleClientTest
{
   @Test
   public void testResponse() throws Exception
   {
      // fill out a query param and execute a get request
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:9095/customers");
      Response response = target.queryParam("name", "Bill").request().get();
      try
      {
         Assert.assertEquals(200, response.getStatus());
         Customer cust = response.readEntity(Customer.class);
         Assert.assertEquals("Bill", cust.getName());
      }
      finally
      {
         response.close();
         client.close();
      }
   }
   @Test
   public void testCustomer() throws Exception
   {
      // fill out a query param and execute a get request
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:9095/customers");
      try
      {
         // extract customer directly expecting success
         Customer cust = target.queryParam("name", "Bill").request().get(Customer.class);
         Assert.assertEquals("Bill", cust.getName());
      }
      finally
      {
         client.close();
      }
   }


   @Test
   public void testTemplate() throws Exception
   {
      // fill out a path param and execute a get request
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:9095/customers/{id}");
      Response response = target.resolveTemplate("id", "12345").request().get();
      try
      {
         Assert.assertEquals(200, response.getStatus());
         Customer cust = response.readEntity(Customer.class);
         Assert.assertEquals("Bill", cust.getName());
      }
      finally
      {
         response.close();
         client.close();
      }
   }

   @Test
   public void testAsync() throws Exception
   {
      // fill out a query param and execute a get request
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:9095/customers");
      try
      {
         // execute in background
         Future<Customer> future = target.queryParam("name", "Bill").request().async().get(Customer.class);
         // wait 10 seconds for a response
         Customer cust = future.get(10, TimeUnit.SECONDS);
         Assert.assertEquals("Bill", cust.getName());
      }
      finally
      {
         client.close();
      }
   }

   @Test
   public void testAsyncCallback() throws Exception
   {
      // fill out a query param and execute a get request
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:9095/customers");
      try
      {
         // execute in background
         final CountDownLatch latch = new CountDownLatch(1);
         target.queryParam("name", "Bill").request().async().get(new InvocationCallback<Customer>()
         {
            @Override
            public void completed(Customer customer)
            {
               System.out.println("Obtained customer: " + customer.getName());
               latch.countDown();
            }

            @Override
            public void failed(Throwable error)
            {
               latch.countDown();
            }
         });
         // await for callback to wake us up
         latch.await(10, TimeUnit.SECONDS);
      }
      finally
      {
         client.close();
      }
   }

   @Path("/customers")
   public interface CustomerProxy
   {
      @GET
      @Produces("application/json")
      Customer getCustomer(@QueryParam("name") String name);

   }

   @Test
   public void testProxy() throws Exception
   {
      Client client = ClientBuilder.newClient();
      CustomerProxy proxy = ((ResteasyClient) client).target("http://localhost:9095").proxy(CustomerProxy.class);
      Customer cust = proxy.getCustomer("Monica");
      Assert.assertEquals("Monica", cust.getName());
      client.close();
   }

}
