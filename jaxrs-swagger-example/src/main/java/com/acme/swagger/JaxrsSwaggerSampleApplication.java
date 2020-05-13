package com.acme.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@SpringBootApplication
@ApplicationPath("/rest/")
public class JaxrsSwaggerSampleApplication extends Application {

    public static void main(String[] args) {
        SpringApplication.run(JaxrsSwaggerSampleApplication.class, args);
    }

}
