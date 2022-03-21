package com.restfully.shop.services;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class ShoppingApplication extends Application
{
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> empty = new HashSet<Class<?>>();

   public ShoppingApplication()
   {
      singletons.add(new CustomerResource());
   }

   @Override
   public Set<Object> getSingletons()
   {
      return singletons;
   }
}
