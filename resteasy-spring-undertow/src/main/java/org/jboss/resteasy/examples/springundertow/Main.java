package org.jboss.resteasy.examples.springundertow;

import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.spring.UndertowJaxrsSpringServer;

public class Main {
    public static void main(String[] args) throws Exception {
        UndertowJaxrsSpringServer server = new UndertowJaxrsSpringServer();
        server.start();

        DeploymentInfo deployment = server.undertowDeployment("classpath:resteasy-spring-mvc-servlet.xml", null);
        deployment.setDeploymentName(Main.class.getName());
        deployment.setContextPath("/");
        deployment.setClassLoader(Main.class.getClassLoader());
        server.deploy(deployment);

        System.out.println("UNDERTOW SERVER STARTED");

        Thread.currentThread().join();

    }
}
