package com.restfully.shop.services;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class ShoppingApplication extends Application
{
   private Set<Object> singletons = new HashSet<Object>();

   public ShoppingApplication()
   {
      singletons.add(new CustomerResource());
      singletons.add(new CustomerChat());
   }

   @Override
   public Set<Object> getSingletons()
   {
      return singletons;
   }
}
