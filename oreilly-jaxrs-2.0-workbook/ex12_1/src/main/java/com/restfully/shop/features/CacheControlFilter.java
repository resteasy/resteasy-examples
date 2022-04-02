package com.restfully.shop.features;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.CacheControl;
import java.io.IOException;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CacheControlFilter implements ContainerResponseFilter
{
   private int maxAge;

   public CacheControlFilter(int maxAge) {
      this.maxAge = maxAge;
   }

   public void filter(ContainerRequestContext req, ContainerResponseContext res)
           throws IOException
   {
      if (req.getMethod().equals("GET")) {
         CacheControl cc = new CacheControl();
         cc.setMaxAge(this.maxAge);
         res.getHeaders().add("Cache-Control", cc);
      }
   }
}