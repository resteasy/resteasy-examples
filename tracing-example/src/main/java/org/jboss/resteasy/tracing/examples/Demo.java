package org.jboss.resteasy.tracing.examples;

import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.logmanager.LogManager;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import java.io.IOException;

public class Demo {
    public static UndertowJaxrsServer buildServer() {
        UndertowJaxrsServer server;
        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        try {
            LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.jboss.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        server = new UndertowJaxrsServer().start();

        ResteasyDeployment deployment = new ResteasyDeploymentImpl();

        deployment.setApplicationClass(TracingApp.class.getName());

        DeploymentInfo di = server.undertowDeployment(deployment);
        di.setClassLoader(TracingApp.class.getClassLoader());
        di.setContextPath("");
        di.setDeploymentName("Resteasy");
        di.getServlets().get("ResteasyServlet").addInitParam(ResteasyContextParameters.RESTEASY_TRACING_TYPE, ResteasyContextParameters.RESTEASY_TRACING_TYPE_ALL)
                .addInitParam(ResteasyContextParameters.RESTEASY_TRACING_THRESHOLD, ResteasyContextParameters.RESTEASY_TRACING_LEVEL_VERBOSE);
        server.deploy(di);
        return server;
    }
}
