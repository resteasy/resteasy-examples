package org.jboss.resteasy.tests.smime;

import org.jboss.resteasy.security.smime.EnvelopedInput;
import org.jboss.resteasy.security.smime.EnvelopedOutput;
import org.jboss.resteasy.security.smime.SignedInput;
import org.jboss.resteasy.security.smime.SignedOutput;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/smime")
public class SMIMEResource
{
   private PrivateKey privateKey;
   private X509Certificate certificate;

   public SMIMEResource(PrivateKey privateKey, X509Certificate certificate)
   {
      this.privateKey = privateKey;
      this.certificate = certificate;
   }

   @Path("encrypted")
   @GET
   public EnvelopedOutput getEncrypted()
   {
      System.out.println("HERE!!!!!");
      Customer cust = new Customer();
      cust.setName("Bill");

      EnvelopedOutput output = new EnvelopedOutput(cust, MediaType.APPLICATION_XML_TYPE);
      output.setCertificate(certificate);
      return output;
   }

   @Path("encrypted")
   @POST
   public void postEncrypted(EnvelopedInput<Customer> input)
   {
      Customer cust = input.getEntity(privateKey, certificate);
      System.out.println("Encrypted Server Input: ");
      System.out.println(cust);
   }

   @Path("signed")
   @GET
   @Produces("multipart/signed")
   public SignedOutput getSigned()
   {
      Customer cust = new Customer();
      cust.setName("Bill");

      SignedOutput output = new SignedOutput(cust, MediaType.APPLICATION_XML_TYPE);
      output.setPrivateKey(privateKey);
      output.setCertificate(certificate);
      return output;
   }

   @Path("signed")
   @POST
   public void postSigned(SignedInput<Customer> input) throws Exception
   {
      Customer cust = input.getEntity();
      System.out.println("Signed Server Input: ");
      System.out.println(cust);
      if (!input.verify(certificate))
      {
         throw new WebApplicationException(500);
      }
   }

   @Path("/encrypted/signed")
   @GET
   public EnvelopedOutput getEncryptedSigned()
   {
      Customer cust = new Customer();
      cust.setName("Bill");

      SignedOutput signed = new SignedOutput(cust, MediaType.APPLICATION_XML_TYPE);
      signed.setCertificate(certificate);
      signed.setPrivateKey(privateKey);

      EnvelopedOutput output = new EnvelopedOutput(signed, "multipart/signed");
      output.setCertificate(certificate);
      return output;
   }

   @Path("/encrypted/signed")
   @POST
   public void postEncryptedSigned(EnvelopedInput<SignedInput<Customer>> input) throws Exception
   {
      SignedInput<Customer> signed = input.getEntity(privateKey, certificate);
      Customer cust = signed.getEntity();
      System.out.println("Encrypted and Signed Server Input: ");
      System.out.println(cust);
      if (!signed.verify(certificate))
      {
         throw new WebApplicationException(500);
      }
   }

}
