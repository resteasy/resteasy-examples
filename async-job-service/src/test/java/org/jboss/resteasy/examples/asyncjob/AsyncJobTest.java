package org.jboss.resteasy.examples.asyncjob;

import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class AsyncJobTest
{
	   
   @Test
   public void testOneway() throws Exception
   {
	  Client client = ClientBuilder.newClient();
	  WebTarget target = client.target("http://localhost:9095/resource?oneway=true");
	  Invocation.Builder builder = target.request();
	  Entity<String> entity = Entity.entity("content", "text/plain");
	  Response response = builder.put(entity);
	   
      Assert.assertEquals(202, response.getStatus());
      
      response.close();
      Thread.sleep(1500);
      
      
	  target = client.target("http://localhost:9095/resource");
	  builder = target.request();   
	  response = builder.get();
      Assert.assertEquals(Integer.toString(1), response.readEntity(String.class));
   }

   @Test
   public void testAsynch() throws Exception
   {
	   
	  Client client = ClientBuilder.newClient();
	  WebTarget target = client.target("http://localhost:9095/resource?asynch=true");
	  Invocation.Builder builder = target.request();
	  Entity<String> entity = Entity.entity("content", "text/plain");
	  Response response = builder.post(entity);
	  
	   
      Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
     
      String jobUrl1 = response.getStringHeaders().getFirst(HttpHeaders.LOCATION);
      System.out.println("jobUrl1: " + jobUrl1);
      
      response.close();
      
      target = client.target(jobUrl1);
	  builder = target.request();
	   
	  response = builder.get(); 
      Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
      
      response.close();
      
      Thread.sleep(1500);
     
      response = builder.get(); 
      Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
      
      Assert.assertEquals("content", response.readEntity(String.class));
   }
}
