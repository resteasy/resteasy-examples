package org.jboss.resteasy.examples.springbasic;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

@Path("/rest/foo")
public class FooResource {

   @Autowired
   FooBean fooBean;

   @GET
   public String getFoo(@Context ServletContext context) {
      return context.getInitParameter("foo");
   }

   @GET
   @Path("/hello")
   public String hello() {
      return fooBean.hello();
   }
}
