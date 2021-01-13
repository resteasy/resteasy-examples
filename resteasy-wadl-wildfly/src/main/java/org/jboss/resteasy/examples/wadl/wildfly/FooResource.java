package org.jboss.resteasy.examples.wadl.wildfly;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/foo")
public class FooResource {
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public ListType post(ListType v) {
        return v;
    }
}
