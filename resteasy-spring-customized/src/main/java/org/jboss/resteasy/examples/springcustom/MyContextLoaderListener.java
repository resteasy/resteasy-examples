package org.jboss.resteasy.examples.springcustom;


import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.plugins.spring.i18n.Messages;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class MyContextLoaderListener extends ContextLoaderListener {
    public MyContextLoaderListener(WebApplicationContext context) {
        super(context);
    }

    @Override
    protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext configurableWebApplicationContext) {
        super.customizeContext(servletContext, configurableWebApplicationContext);

        ResteasyDeployment deployment = (ResteasyDeployment) servletContext.getAttribute(ResteasyDeployment.class.getName());
        if (deployment == null) {
            throw new RuntimeException(Messages.MESSAGES.deploymentIsNull());
        }

        SpringBeanProcessor processor = new SpringBeanProcessor(deployment);
        configurableWebApplicationContext.addBeanFactoryPostProcessor(processor);
        configurableWebApplicationContext.addApplicationListener(processor);
    }
}
