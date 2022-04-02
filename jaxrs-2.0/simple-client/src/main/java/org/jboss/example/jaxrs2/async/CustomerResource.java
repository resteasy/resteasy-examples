package org.jboss.example.jaxrs2.async;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/customers")
public class CustomerResource
{
   @GET
   @Produces("application/json")
   public Customer getByName(@QueryParam("name") String name)
   {
      return new Customer(name);
   }

   @GET
   @Path("{id}")
   @Produces("application/json")
   public Customer getById(@PathParam("id") String id)
   {
      return new Customer("Bill");
   }

}
