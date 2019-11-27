package org.jboss.resteasy.examples.springcustom;

import org.jboss.resteasy.plugins.server.servlet.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


public class MyWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // `ResteasyBootstrap` is not mandatory if you want to setup `ResteasyContext` and `ResteasyDeployment` manually.
        servletContext.addListener(ResteasyBootstrap.class);

        // Create Spring context.
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MyConfig.class);

        // Implement our own `ContextLoaderListener`, so we can implement our own `SpringBeanProcessor` if necessary.
        servletContext.addListener(new MyContextLoaderListener(context));

        // We can use `HttpServletDispatcher` or `FilterDispatcher` here, and implement our own solution.
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("resteasy-dispatcher", new HttpServletDispatcher());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/rest/*");
    }
}
