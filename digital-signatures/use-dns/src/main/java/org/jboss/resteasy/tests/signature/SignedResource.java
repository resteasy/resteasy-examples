package org.jboss.resteasy.tests.signature;

import org.jboss.resteasy.annotations.security.doseta.Signed;
import org.jboss.resteasy.annotations.security.doseta.Verify;
import org.jboss.resteasy.security.doseta.DKIMSignature;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/signed")
public class SignedResource
{
   /**
    * Sign a returned message using a private key named "test._domainKey.samplezone.org"
    * found in the key store.
    *
    * @return
    */
   @GET
   @Signed(selector = "anil", domain="server.com")
   @Produces("text/plain")
   public String hello()
   {
      return "hello world";
   }

   /**
    * Verify a posted signature. Inject it and print it out too.
    *
    * @param signature
    * @param input
    */
    @POST
    @Consumes("text/plain")
    @Verify
    public void post(@HeaderParam(DKIMSignature.DKIM_SIGNATURE) DKIMSignature signature, String input)
    {
        if (signature == null) {
            throw new RuntimeException("signature was null");
        }
        if (!"hello world".equals(input)) {
            throw new RuntimeException("Expected \"hello world\" got \"" + input + "\"");
        }
    }

}
