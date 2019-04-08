package org.jboss.resteasy.tracing.examples;

import javax.ws.rs.Path;

@Path("/locator")
public class FooLocator {
    @Path("foo")
    public Foo getFoo() {
        return new Foo();
    }
}
