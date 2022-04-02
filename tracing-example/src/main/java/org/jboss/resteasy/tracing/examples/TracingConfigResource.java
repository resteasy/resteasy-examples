package org.jboss.resteasy.tracing.examples;

import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.tracing.RESTEasyTracingLogger;
import org.jboss.resteasy.tracing.api.RESTEasyTracing;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;

@Path("/")
public class TracingConfigResource extends Application {

    @GET
    @Path("/type")
    public String type(@Context ResteasyDeployment deployment) {
        return RESTEasyTracingLogger.getTracingConfig(deployment.getProviderFactory()).toString();
    }

    @GET
    @Path("/level")
    public String level(@Context ResteasyDeployment deployment) {
        return RESTEasyTracingLogger.getTracingThreshold(deployment.getProviderFactory()).toString();
    }

    @GET
    @Path("/logger")
    public String logger(@Context HttpRequest request) throws NoSuchMethodException {
        RESTEasyTracingLogger logger = (RESTEasyTracingLogger) request.getAttribute(RESTEasyTracing.PROPERTY_NAME);
        if (logger == null) {
            return "";
        } else {
            return RESTEasyTracingLogger.class.getName();
        }
    }

}
