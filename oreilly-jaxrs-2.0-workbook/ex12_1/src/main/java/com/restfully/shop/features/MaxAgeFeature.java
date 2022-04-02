package com.restfully.shop.features;

import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Provider
public class MaxAgeFeature implements DynamicFeature
{

   public void configure(ResourceInfo ri, FeatureContext ctx) {
      MaxAge max = ri.getResourceMethod().getAnnotation(MaxAge.class);
      if (max == null) return;
      CacheControlFilter filter = new CacheControlFilter(max.value());
      ctx.register(filter);
   }
}
