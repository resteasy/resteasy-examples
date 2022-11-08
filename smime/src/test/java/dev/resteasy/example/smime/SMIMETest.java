package dev.resteasy.example.smime;

import java.io.InputStream;
import java.net.URI;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.security.PemUtils;
import org.jboss.resteasy.security.smime.EnvelopedInput;
import org.jboss.resteasy.security.smime.EnvelopedOutput;
import org.jboss.resteasy.security.smime.SignedInput;
import org.jboss.resteasy.security.smime.SignedOutput;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@ExtendWith(ArquillianExtension.class)
@RunAsClient
public class SMIMETest {
    private static PrivateKey privateKey;
    private static X509Certificate cert;
    private static Client client;

    @ArquillianResource
    private URI uri;

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, SMIMETest.class.getSimpleName() + ".war")
                .addClasses(Customer.class, SMIMEApplication.class, SMIMEResource.class)
                .addAsWebInfResource(SMIMETest.class.getResource("/cert.pem"), "classes/cert.pem")
                .addAsWebInfResource(SMIMETest.class.getResource("/private.pem"), "classes/private.pem")
                // Required until WFLY-13917 is resolved
                .addAsManifestResource(new StringAsset("Dependencies: org.bouncycastle import\n"), "MANIFEST.MF");
    }

    @BeforeAll
    public static void setup() throws Exception {
        InputStream certPem = Thread.currentThread().getContextClassLoader().getResourceAsStream("cert.pem");
        Assertions.assertNotNull(certPem);
        cert = PemUtils.decodeCertificate(certPem);

        InputStream privatePem = Thread.currentThread().getContextClassLoader().getResourceAsStream("private.pem");
        privateKey = PemUtils.decodePrivateKey(privatePem);
        client = ((ResteasyClientBuilder) (ClientBuilder.newBuilder())).connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS).build();
    }

    @AfterAll
    public static void shutdown() throws Exception {
        client.close();
    }

    @Test
    public void testEncryptedGet() throws Exception {
        WebTarget target = client.target(generateUri("smime/encrypted"));
        EnvelopedInput input = target.request().get(EnvelopedInput.class);
        Customer cust = (Customer) input.getEntity(Customer.class, privateKey, cert);
        System.out.println("Encrypted Message From Server:");
        System.out.println(cust);
    }

    @Test
    public void testEncryptedPost() throws Exception {
        WebTarget target = client.target(generateUri("smime/encrypted"));
        Customer cust = new Customer();
        cust.setName("Bill");
        EnvelopedOutput output = new EnvelopedOutput(cust, "application/xml");
        output.setCertificate(cert);
        Response res = target.request().post(Entity.entity(output, "application/pkcs7-mime"));
        Assertions.assertEquals(204, res.getStatus());
        res.close();

    }

    @Test
    public void testSigned() throws Exception {
        WebTarget target = client.target(generateUri("smime/signed"));
        SignedInput input = target.request().get(SignedInput.class);
        Customer cust = (Customer) input.getEntity(Customer.class);
        System.out.println("Signed Message From Server: ");
        System.out.println(cust);
        input.verify(cert);

    }

    @Test
    public void testSignedPost() throws Exception {
        WebTarget target = client.target(generateUri("smime/signed"));
        Customer cust = new Customer();
        cust.setName("Bill");
        SignedOutput output = new SignedOutput(cust, "application/xml");
        output.setPrivateKey(privateKey);
        output.setCertificate(cert);
        Response res = target.request().post(Entity.entity(output, "multipart/signed"));
        Assertions.assertEquals(204, res.getStatus());
        res.close();
    }

    @Test
    public void testEncryptedAndSignedGet() throws Exception {
        WebTarget target = client.target(generateUri("smime/encrypted/signed"));
        EnvelopedInput<Customer> enveloped = target.request().get(new GenericType<>() {
        });
        SignedInput<Customer> signed = enveloped.getEntity(SignedInput.class, privateKey, cert);
        Customer cust = signed.getEntity(Customer.class);
        System.out.println(cust);
        Assertions.assertTrue(signed.verify(cert));
    }

    @Test
    public void testEncryptedSignedPost() throws Exception {
        WebTarget target = client.target(generateUri("smime/encrypted/signed"));
        Customer cust = new Customer();
        cust.setName("Bill");
        SignedOutput signed = new SignedOutput(cust, "application/xml");
        signed.setPrivateKey(privateKey);
        signed.setCertificate(cert);
        EnvelopedOutput output = new EnvelopedOutput(signed, "multipart/signed");
        output.setCertificate(cert);
        Response res = target.request().post(Entity.entity(output, "application/pkcs7-mime"));
        Assertions.assertEquals(204, res.getStatus());
        res.close();
    }

    private UriBuilder generateUri(final String path) {
        return UriBuilder.fromUri(uri).path(path);
    }

}
