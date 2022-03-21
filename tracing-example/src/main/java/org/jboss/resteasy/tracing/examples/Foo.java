package org.jboss.resteasy.tracing.examples;

import jakarta.ws.rs.GET;

public class Foo {
    @GET
    public String get() {
        return "{|FOO|}";
    }
}
