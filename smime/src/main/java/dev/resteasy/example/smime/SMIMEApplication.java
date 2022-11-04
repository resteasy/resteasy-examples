package dev.resteasy.example.smime;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import org.jboss.resteasy.security.PemUtils;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@ApplicationPath("/")
public class SMIMEApplication extends Application {
    private final Set<Object> resources = new HashSet<>();

    public SMIMEApplication() throws Exception {
        InputStream privatePem = Thread.currentThread().getContextClassLoader().getResourceAsStream("private.pem");
        PrivateKey privateKey = PemUtils.decodePrivateKey(privatePem);

        InputStream certPem = Thread.currentThread().getContextClassLoader().getResourceAsStream("cert.pem");
        X509Certificate cert = PemUtils.decodeCertificate(certPem);

        SMIMEResource resource = new SMIMEResource(privateKey, cert);
        resources.add(resource);
    }

    @Override
    public Set<Object> getSingletons() {
        return resources;
    }
}
