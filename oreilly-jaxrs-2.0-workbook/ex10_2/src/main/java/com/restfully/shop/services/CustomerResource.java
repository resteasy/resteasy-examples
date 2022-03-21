package com.restfully.shop.services;

import com.restfully.shop.domain.Customer;
import com.restfully.shop.domain.Customers;
import org.jboss.resteasy.annotations.providers.jaxb.Formatted;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/customers")
public class CustomerResource
{
   private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
   private AtomicInteger idCounter = new AtomicInteger();

   @POST
   @Consumes("application/xml")
   public Response createCustomer(Customer customer, @Context UriInfo uriInfo)
   {
      customer.setId(idCounter.incrementAndGet());
      customerDB.put(customer.getId(), customer);
      System.out.println("Created customer " + customer.getId());
      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
      builder.path(Integer.toString(customer.getId()));
      return Response.created(builder.build()).build();

   }

   @GET
   @Produces("application/xml")
   @Formatted
   public Customers getCustomers(@QueryParam("start") int start,
                                 @QueryParam("size") @DefaultValue("2") int size,
                                 @Context UriInfo uriInfo)
   {
      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
      builder.queryParam("start", "{start}");
      builder.queryParam("size", "{size}");

      ArrayList<Customer> list = new ArrayList<Customer>();
      ArrayList<Link> links = new ArrayList<Link>();
      synchronized (customerDB)
      {
         int i = 0;
         for (Customer customer : customerDB.values())
         {
            if (i >= start && i < start + size) list.add(customer);
            i++;
         }
         // next link
         if (start + size < customerDB.size())
         {
            int next = start + size;
            URI nextUri = builder.clone().build(next, size);
            Link nextLink = Link.fromUri(nextUri).rel("next").type("application/xml").build();
            links.add(nextLink);
         }
         // previous link
         if (start > 0)
         {
            int previous = start - size;
            if (previous < 0) previous = 0;
            URI previousUri = builder.clone().build(previous, size);
            Link previousLink = Link.fromUri(previousUri).rel("previous").type("application/xml").build();
            links.add(previousLink);
         }
      }
      Customers customers = new Customers();
      customers.setCustomers(list);
      customers.setLinks(links);
      return customers;
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

}
