package org.jboss.resteasy.examples.springcustom;

import org.jboss.resteasy.plugins.spring.SpringBeanProcessorServletAware;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


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
