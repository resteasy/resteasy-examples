package org.jboss.resteasy.examples.springcustom;

import org.jboss.resteasy.plugins.spring.SpringBeanProcessorServletAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import jakarta.ws.rs.Path;

@Configuration
@ComponentScan(basePackages = "org.jboss.resteasy.examples.springcustom",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = Path.class
))
public class MyConfig {

    @Bean
    SpringBeanProcessorServletAware servletAware() {
        return new SpringBeanProcessorServletAware();
    }
}
