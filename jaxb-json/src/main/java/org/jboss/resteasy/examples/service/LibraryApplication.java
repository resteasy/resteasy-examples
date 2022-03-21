package org.jboss.resteasy.examples.service;

import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class LibraryApplication extends Application
{
   HashSet<Object> singletons = new HashSet<Object>();

   public LibraryApplication()
   {
      singletons.add(new Library());
   }

   @Override
   public Set<Class<?>> getClasses()
   {
      HashSet<Class<?>> set = new HashSet<Class<?>>();
      return set;
   }

   @Override
   public Set<Object> getSingletons()
   {
      return singletons;  
   }
}
