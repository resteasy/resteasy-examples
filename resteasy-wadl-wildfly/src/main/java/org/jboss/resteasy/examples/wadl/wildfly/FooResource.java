package org.jboss.resteasy.examples.wadl.wildfly;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/foo")
public class FooResource {
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public ListType post(ListType v) {
        return v;
    }
}
