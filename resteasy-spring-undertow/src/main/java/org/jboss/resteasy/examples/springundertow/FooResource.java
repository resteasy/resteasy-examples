package org.jboss.resteasy.examples.springundertow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

@Component
@Path("/rest/foo")
public class FooResource {

   @Autowired
   HelloWorldBean bean;

   @GET
   public String getFoo() {
      return bean.get();
   }
}
