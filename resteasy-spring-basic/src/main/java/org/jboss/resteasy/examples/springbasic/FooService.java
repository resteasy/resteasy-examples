package org.jboss.resteasy.examples.springbasic;

import org.springframework.stereotype.Component;

@Component
public class FooService {
    public String hello() {
        return "Hello, world!";
    }
}
