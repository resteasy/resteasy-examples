package com.restfully.shop.services;

import com.restfully.shop.domain.Customer;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.text.DateFormat;
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
   }

   @POST
   @Produces("text/html")
   public Response createCustomer(@FormParam("firstname") String first,
                                  @FormParam("lastname") String last)
   {
      Customer customer = new Customer();
      customer.setId(idCounter.incrementAndGet());
      customer.setFirstName(first);
      customer.setLastName(last);
      customerDB.put(customer.getId(), customer);
      System.out.println("Created customer " + customer.getId());
      String output = "Created customer <a href=\"customers/" + customer.getId() + "\">" + customer.getId() + "</a>";
      String lastVisit = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
      URI location = URI.create("/customers/" + customer.getId());
      return Response.created(location)
              .entity(output)
              .cookie(new NewCookie("last-visit", lastVisit))
              .build();

   }

   @GET
   @Path("{id}")
   @Produces("text/plain")
   public Response getCustomer(@PathParam("id") int id,
                               @HeaderParam("User-Agent") String userAgent,
                               @CookieParam("last-visit") String date)
   {
      final Customer customer = customerDB.get(id);
      if (customer == null)
      {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
      String output = "User-Agent: " + userAgent + "\r\n";
      output += "Last visit: " + date + "\r\n\r\n";
      output += "Customer: " + customer.getFirstName() + " " + customer.getLastName();
      String lastVisit = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
      return Response.ok(output)
              .cookie(new NewCookie("last-visit", lastVisit))
              .build();
   }

}
