package com.acme.swagger;


import com.acme.swagger.rest.DummyService;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/rest/")
public class JaxrsSwaggerSampleApplication extends Application {

    protected Set<Object> singletons = new HashSet<Object>();

    public JaxrsSwaggerSampleApplication()
    {
        singletons.add(new DummyService());
    }

    @Override
    public Set<Object> getSingletons()
    {
        return singletons;
    }

}
