package org.jboss.resteasy.examples.wadl.wildfly;

import org.jboss.resteasy.wadl.ResteasyWadlDefaultResource;
import org.jboss.resteasy.wadl.ResteasyWadlWriter;

import jakarta.ws.rs.Path;

@Path("/")
public class MyWadlResource extends ResteasyWadlDefaultResource {

    public MyWadlResource() {
        ResteasyWadlWriter.ResteasyWadlGrammar wadlGrammar = new ResteasyWadlWriter.ResteasyWadlGrammar();
        wadlGrammar.enableSchemaGeneration();
        getWadlWriter().setWadlGrammar(wadlGrammar);

    }
}
