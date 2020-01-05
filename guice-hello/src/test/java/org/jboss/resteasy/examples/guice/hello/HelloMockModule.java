package org.jboss.resteasy.examples.guice.hello;

import com.google.inject.AbstractModule;

public class HelloMockModule extends AbstractModule
{
   @Override
   protected void configure()
   {
      bind(HelloResource.class);
      bind(Greeter.class).to(DefaultGreeter.class);
   }
}
