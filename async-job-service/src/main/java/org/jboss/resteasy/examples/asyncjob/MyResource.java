package org.jboss.resteasy.examples.asyncjob;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/resource")
public class MyResource
{
   private static int count = 0;


   @POST
   @Produces("text/plain")
   @Consumes("text/plain")
   public String post(String content) throws Exception
   {
      Thread.sleep(1000);
      return content;
   }

   @GET
   @Produces("text/plain")
   public String get()
   {
      return Integer.toString(count);
   }

   @PUT
   @Consumes("text/plain")
   public void put(String content) throws Exception
   {
      System.out.println("IN PUT!!!!");
      Thread.sleep(1000);
      System.out.println("******* countdown complete ****");
      count++;
   }
}
