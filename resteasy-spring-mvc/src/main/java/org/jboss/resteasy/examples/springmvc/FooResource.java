package org.jboss.resteasy.examples.springmvc;

import jakarta.servlet.ServletContext;
import org.springframework.stereotype.Component;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;


@Component
@Path("/rest/foo")
public class FooResource {

   @GET
   public String getFoo(@Context ServletContext context) {
      return context.getInitParameter("foo");
   }
}
