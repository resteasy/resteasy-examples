package org.jboss.resteasy.examples.springrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooResource {

   @Autowired
   FooBean fooBean;

   @GetMapping("/hello")
   public String hello() {
      return fooBean.hello();
   }
}
