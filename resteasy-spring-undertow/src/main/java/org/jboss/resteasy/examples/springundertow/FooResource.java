package org.jboss.resteasy.examples.springundertow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

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
