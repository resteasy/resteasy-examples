package com.restfully.shop.services;

import com.restfully.shop.domain.Customer;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/customers")
public class CustomerResource
{
   private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
   private AtomicInteger idCounter = new AtomicInteger();

   public CustomerResource()
   {
      Customer customer;
      int id = 1;

      customer = new Customer();
      customer.setId(id);
      customer.setFirstName("Bill");
      customer.setLastName("Burke");
      customer.setStreet("263 Clarendon Street");
      customer.setCity("Boston");
      customer.setState("MA");
      customer.setZip("02115");
      customer.setCountry("USA");
      customerDB.put(id++, customer);
   }

   @POST
   @Consumes("application/xml")
   public Response createCustomer(Customer customer)
   {
      customer.setId(idCounter.incrementAndGet());
      customerDB.put(customer.getId(), customer);
      System.out.println("Created customer " + customer.getId());
      return Response.created(URI.create("/customers/" + customer.getId())).build();

   }

   @GET
   @Path("{id}")
   @Produces("application/xml")
   public Response getCustomer(@PathParam("id") int id,
                               @HeaderParam("If-None-Match") String sent,
                               @Context Request request)
   {
      Customer cust = customerDB.get(id);
      if (cust == null)
      {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }

      if (sent == null) System.out.println("No If-None-Match sent by client");

      EntityTag tag = new EntityTag(Integer.toString(cust.hashCode()));

      CacheControl cc = new CacheControl();
      cc.setMaxAge(5);


      Response.ResponseBuilder builder = request.evaluatePreconditions(tag);
      if (builder != null)
      {
         System.out.println("** revalidation on the server was successful");
         builder.cacheControl(cc);
         return builder.build();
      }


      // Preconditions not met!

      cust.setLastViewed(new Date().toString());
      builder = Response.ok(cust, "application/xml");
      builder.cacheControl(cc);
      builder.tag(tag);
      return builder.build();
   }


   @Path("{id}")
   @PUT
   @Consumes("application/xml")
   public Response updateCustomer(@PathParam("id") int id,
                                  @Context Request request,
                                  Customer update)
   {
      Customer cust = customerDB.get(id);
      if (cust == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
      EntityTag tag = new EntityTag(Integer.toString(cust.hashCode()));

      Response.ResponseBuilder builder =
              request.evaluatePreconditions(tag);

      if (builder != null)
      {
         // Preconditions not met!
         return builder.build();
      }

      // Preconditions met, perform update

      cust.setFirstName(update.getFirstName());
      cust.setLastName(update.getLastName());
      cust.setStreet(update.getStreet());
      cust.setState(update.getState());
      cust.setZip(update.getZip());
      cust.setCountry(update.getCountry());


      builder = Response.noContent();
      return builder.build();
   }
}
