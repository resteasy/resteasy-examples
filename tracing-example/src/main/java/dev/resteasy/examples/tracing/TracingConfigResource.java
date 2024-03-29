package dev.resteasy.examples.tracing;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Context;

import org.jboss.resteasy.tracing.RESTEasyTracingLogger;
import org.jboss.resteasy.tracing.api.RESTEasyTracing;

@Path("/")
public class TracingConfigResource {

    @GET
    @Path("/type")
    public String type(@Context Configuration config) {
        return RESTEasyTracingLogger.getTracingConfig(config);
    }

    @GET
    @Path("/level")
    public String level(@Context Configuration config) {
        return RESTEasyTracingLogger.getTracingThreshold(config);
    }

    @GET
    @Path("/logger")
    public String logger(@Context HttpServletRequest request) {
        RESTEasyTracingLogger logger = (RESTEasyTracingLogger) request.getAttribute(RESTEasyTracing.PROPERTY_NAME);
        if (logger == null) {
            return "";
        } else {
            return RESTEasyTracingLogger.class.getName();
        }
    }

}
