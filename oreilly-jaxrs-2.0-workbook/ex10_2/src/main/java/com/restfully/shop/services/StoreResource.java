package com.restfully.shop.services;

import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/shop")
public class StoreResource
{
   @HEAD
   public Response head(@Context UriInfo uriInfo)
   {
      UriBuilder absolute = uriInfo.getBaseUriBuilder();
      URI customerUrl = absolute.clone().path(CustomerResource.class).build();
      URI orderUrl = absolute.clone().path(OrderResource.class).build();

      Response.ResponseBuilder builder = Response.ok();
      Link customers = Link.fromUri(customerUrl).rel("customers").type("application/xml").build();
      Link orders = Link.fromUri(orderUrl).rel("orders").type("application/xml").build();
      builder.links(customers, orders);
      return builder.build();
   }
}
