package com.restfully.shop.services;

import com.restfully.shop.domain.Customer;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.net.URI;
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
      Customer customer = new Customer();
      customer.setId(1);
      customer.setFirstName("Bill");
      customer.setLastName("Burke");
      customer.setStreet("263 Clarendon Street");
      customer.setCity("Boston");
      customer.setState("MA");
      customer.setZip("02115");
      customer.setCountry("USA");
      customerDB.put(1, customer);
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
   @Produces({"application/xml", "application/json"})
   public Customer getCustomer(@PathParam("id") int id)
   {
      Customer customer = customerDB.get(id);
      if (customer == null)
      {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
      return customer;
   }


   @GET
   @Path("{id}")
   @Produces("text/plain")
   public String getCustomerString(@PathParam("id") int id)
   {
      return getCustomer(id).toString();
   }

   @GET
   @Path("{id}")
   @Produces("text/html")
   public String getCustomerHtml(@PathParam("id") int id)
   {
      return "<h1>Customer As HTML</h1><pre>" + getCustomer(id).toString() + "</pre>";
   }
}
