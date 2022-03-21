package org.jboss.resteasy.tests.smime;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.security.PemUtils;
import org.jboss.resteasy.security.smime.EnvelopedInput;
import org.jboss.resteasy.security.smime.EnvelopedOutput;
import org.jboss.resteasy.security.smime.SignedInput;
import org.jboss.resteasy.security.smime.SignedOutput;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class SMIMETest
{
   private static PrivateKey privateKey;
   private static X509Certificate cert;
   private static Client client;

   @BeforeClass
   public static void setup() throws Exception
   {
      InputStream certPem = Thread.currentThread().getContextClassLoader().getResourceAsStream("cert.pem");
      Assert.assertNotNull(certPem);
      cert = PemUtils.decodeCertificate(certPem);

      InputStream privatePem = Thread.currentThread().getContextClassLoader().getResourceAsStream("private.pem");
      privateKey = PemUtils.decodePrivateKey(privatePem);
      client = ((ResteasyClientBuilder)(ClientBuilder.newBuilder())).connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
   }

   @AfterClass
   public static void shutdown() throws Exception
   {
      client.close();
   }

   @Test
   public void testEncryptedGet() throws Exception
   {
      WebTarget target = client.target("http://localhost:9095/smime/encrypted");
      EnvelopedInput input = target.request().get(EnvelopedInput.class);
      Customer cust = (Customer)input.getEntity(Customer.class, privateKey, cert);
      System.out.println("Encrypted Message From Server:");
      System.out.println(cust);
   }
   @Test
   public void testEncryptedPost() throws Exception
   {
      WebTarget target = client.target("http://localhost:9095/smime/encrypted");
      Customer cust = new Customer();
      cust.setName("Bill");
      EnvelopedOutput output = new EnvelopedOutput(cust, "application/xml");
      output.setCertificate(cert);
      Response res = target.request().post(Entity.entity(output, "application/pkcs7-mime"));
      Assert.assertEquals(204, res.getStatus());
      res.close();

   }

   @Test
   public void testSigned() throws Exception
   {
      WebTarget target = client.target("http://localhost:9095/smime/signed");
      SignedInput input = target.request().get(SignedInput.class);
      Customer cust = (Customer)input.getEntity(Customer.class);
      System.out.println("Signed Message From Server: ");
      System.out.println(cust);
      input.verify(cert);

   }

   @Test
   public void testSignedPost() throws Exception
   {
      WebTarget target = client.target("http://localhost:9095/smime/signed");
      Customer cust = new Customer();
      cust.setName("Bill");
      SignedOutput output = new SignedOutput(cust, "application/xml");
      output.setPrivateKey(privateKey);
      output.setCertificate(cert);
      Response res = target.request().post(Entity.entity(output, "multipart/signed"));
      Assert.assertEquals(204, res.getStatus());
      res.close();
   }

   @Test
   public void testEncryptedAndSignedGet() throws Exception
   {
      WebTarget target = client.target("http://localhost:9095/smime/encrypted/signed");
      EnvelopedInput enveloped = target.request().get(EnvelopedInput.class);
      SignedInput signed = (SignedInput)enveloped.getEntity(SignedInput.class, privateKey, cert);
      Customer cust = (Customer)signed.getEntity(Customer.class);
      System.out.println(cust);
      Assert.assertTrue(signed.verify(cert));
   }

   @Test
   public void testEncryptedSignedPost() throws Exception
   {
      WebTarget target = client.target("http://localhost:9095/smime/encrypted/signed");
      Customer cust = new Customer();
      cust.setName("Bill");
      SignedOutput signed = new SignedOutput(cust, "application/xml");
      signed.setPrivateKey(privateKey);
      signed.setCertificate(cert);
      EnvelopedOutput output = new EnvelopedOutput(signed, "multipart/signed");
      output.setCertificate(cert);
      Response res = target.request().post(Entity.entity(output, "application/pkcs7-mime"));
      Assert.assertEquals(204, res.getStatus());
      res.close();
   }


}
