package org.jboss.resteasy.examples.springrest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring")
public class SpringRestAnnotationResource {

    @GetMapping("/")
    public String get() {
        return "Spring is coming!";
    }
}
