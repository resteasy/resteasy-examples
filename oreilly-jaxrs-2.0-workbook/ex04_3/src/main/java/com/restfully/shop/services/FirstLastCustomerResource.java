package com.restfully.shop.services;

import com.restfully.shop.domain.Customer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import jakarta.xml.parsers.DocumentBuilder;
import jakarta.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class FirstLastCustomerResource
{
   private Map<String, Customer> customerDB =
           new ConcurrentHashMap<String, Customer>();

   @GET
   @Path("{first}-{last}")
   @Produces("application/xml")
   public StreamingOutput getCustomer(@PathParam("first") String firstName,
                                      @PathParam("last") String lastName)
   {
      final Customer customer = customerDB.get(firstName + "-" + lastName);
      if (customer == null)
      {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
      return new StreamingOutput()
      {
         public void write(OutputStream outputStream) throws IOException, WebApplicationException
         {
            outputCustomer(outputStream, customer);
         }
      };
   }

   @PUT
   @Path("{first}-{last}")
   @Consumes("application/xml")
   public void updateCustomer(@PathParam("first") String firstName,
                              @PathParam("last") String lastName,
                              InputStream is)
   {
      Customer update = readCustomer(is);
      Customer current = customerDB.get(firstName + "-" + lastName);
      if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

      current.setFirstName(update.getFirstName());
      current.setLastName(update.getLastName());
      current.setStreet(update.getStreet());
      current.setState(update.getState());
      current.setZip(update.getZip());
      current.setCountry(update.getCountry());
   }

   @POST
   @Consumes("application/xml")
   public Response createCustomer(InputStream is)
   {
      Customer customer = readCustomer(is);
      String index = customer.getFirstName() + "-" + customer.getLastName();
      customerDB.put(index, customer);
      System.out.println("Created customer " + index);
      return Response.created(URI.create("/customers/northamerica-db/" + index)).build();

   }


   protected void outputCustomer(OutputStream os, Customer cust) throws IOException
   {
      PrintStream writer = new PrintStream(os);
      writer.println("<customer>");
      writer.println("   <first-name>" + cust.getFirstName() + "</first-name>");
      writer.println("   <last-name>" + cust.getLastName() + "</last-name>");
      writer.println("   <street>" + cust.getStreet() + "</street>");
      writer.println("   <city>" + cust.getCity() + "</city>");
      writer.println("   <state>" + cust.getState() + "</state>");
      writer.println("   <zip>" + cust.getZip() + "</zip>");
      writer.println("   <country>" + cust.getCountry() + "</country>");
      writer.println("</customer>");
   }

   protected Customer readCustomer(InputStream is)
   {
      try
      {
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = builder.parse(is);
         Element root = doc.getDocumentElement();
         Customer cust = new Customer();
         NodeList nodes = root.getChildNodes();
         for (int i = 0; i < nodes.getLength(); i++)
         {
            Element element = (Element) nodes.item(i);
            if (element.getTagName().equals("first-name"))
            {
               cust.setFirstName(element.getTextContent());
            }
            else if (element.getTagName().equals("last-name"))
            {
               cust.setLastName(element.getTextContent());
            }
            else if (element.getTagName().equals("street"))
            {
               cust.setStreet(element.getTextContent());
            }
            else if (element.getTagName().equals("city"))
            {
               cust.setCity(element.getTextContent());
            }
            else if (element.getTagName().equals("state"))
            {
               cust.setState(element.getTextContent());
            }
            else if (element.getTagName().equals("zip"))
            {
               cust.setZip(element.getTextContent());
            }
            else if (element.getTagName().equals("country"))
            {
               cust.setCountry(element.getTextContent());
            }
         }
         return cust;
      }
      catch (Exception e)
      {
         throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
      }
   }
}
