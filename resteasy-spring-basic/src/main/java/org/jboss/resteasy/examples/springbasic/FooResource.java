package org.jboss.resteasy.examples.springbasic;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/rest/foo")
public class FooResource {

   @Autowired
   FooService fooService;

   @GET
   public String getFoo(@Context ServletContext context) {
      return context.getInitParameter("foo");
   }

   @GET
   @Path("/hello")
   public String hello() {
      return fooService.hello();
   }
}
