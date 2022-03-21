package com.restfully.shop.features;

import javax.annotation.Priority;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@OTPAuthenticated
@Priority(Priorities.AUTHENTICATION)
public class OneTimePasswordAuthenticator implements ContainerRequestFilter
{
   protected Map<String, String> userSecretMap;

   public OneTimePasswordAuthenticator(Map<String, String> userSecretMap)
   {
      this.userSecretMap = userSecretMap;
   }

   @Override
   public void filter(ContainerRequestContext requestContext) throws IOException
   {
      String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
      if (authorization == null) throw new NotAuthorizedException("OTP");

      String[] split = authorization.split(" ");
      final String user = split[0];
      String otp = split[1];

      String secret = userSecretMap.get(user);
      if (secret == null) throw new NotAuthorizedException("OTP");

      String regen = OTP.generateToken(secret);
      if (!regen.equals(otp)) throw new NotAuthorizedException("OTP");

      final SecurityContext securityContext = requestContext.getSecurityContext();
      requestContext.setSecurityContext(new SecurityContext()
      {
         @Override
         public Principal getUserPrincipal()
         {
            return new Principal()
            {
               @Override
               public String getName()
               {
                  return user;
               }
            };
         }

         @Override
         public boolean isUserInRole(String role)
         {
            return false;
         }

         @Override
         public boolean isSecure()
         {
            return securityContext.isSecure();
         }

         @Override
         public String getAuthenticationScheme()
         {
            return "OTP";
         }
      });
   }
}
