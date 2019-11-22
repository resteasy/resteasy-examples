package org.jboss.resteasy.examples.springmvc;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Component
@Path("/rest/foo")
public class FooResource {

   @GET
   public String getFoo(@Context ServletContext context) {
      return context.getInitParameter("foo");
   }
}
