package org.jboss.resteasy.examples.springundertow;

import org.springframework.stereotype.Component;

@Component
public class HelloWorldBean {
    public String get() {
        return "Hello, world!";
    }
}
