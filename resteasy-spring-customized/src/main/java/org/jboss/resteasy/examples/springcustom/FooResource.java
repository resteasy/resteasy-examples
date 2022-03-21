package org.jboss.resteasy.examples.springcustom;

import org.jboss.resteasy.plugins.spring.SpringBeanProcessorServletAware;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;


@Path("/rest")
public class FooResource {

    @Inject
    SpringBeanProcessorServletAware servletAware;

    @GET
    @Path("/servlet")
    public String get() {
        return servletAware.getDispatcher().toString();
    }
}
