package org.jboss.resteasy.examples.wadl.wildfly;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class MyApplication extends Application {

//    private Set<Object> singletons = new HashSet<>();
//    private Set<Class<?>> classes = new HashSet<>();
//
//    public MyApplication() {
//
//        classes.add(FooResource.class);
//
//        ResteasyWadlDefaultResource defaultResource = new ResteasyWadlDefaultResource();
//        ResteasyWadlWriter.ResteasyWadlGrammar wadlGrammar = new ResteasyWadlWriter.ResteasyWadlGrammar();
//        wadlGrammar.enableSchemaGeneration();
//        defaultResource.getWadlWriter().setWadlGrammar(wadlGrammar);
//
//        singletons.add(defaultResource);
//
//    }
//
//    @Override
//    public Set<Class<?>> getClasses() {
//        return classes;
//    }
//
//    @Override
//    public Set<Object> getSingletons() {
//        return singletons;
//    }
}
