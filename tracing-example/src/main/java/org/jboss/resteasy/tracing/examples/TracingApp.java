package org.jboss.resteasy.tracing.examples;

import org.jboss.resteasy.plugins.interceptors.GZIPDecodingInterceptor;
import org.jboss.resteasy.plugins.interceptors.GZIPEncodingInterceptor;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class TracingApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set set = new HashSet<Class<?>>();
        set.add(HttpMethodOverride.class);
        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        Set set = new HashSet<Class<?>>();
        set.add(new TracingConfigResource());
        set.add(new FooLocator());
        set.add(new GZIPDecodingInterceptor());
        set.add(new GZIPEncodingInterceptor());
        return set;
    }
}
