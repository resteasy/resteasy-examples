package org.jboss.resteasy.wadl.testing.locator;

import jakarta.ws.rs.Path;

/**
 * @author <a href="mailto:l.weinan@gmail.com">Weinan Li</a>
 */
@Path("/parent")
public class Parent {
    @Path("/child")
    public Child child() {
        return new Child();
    }
}
