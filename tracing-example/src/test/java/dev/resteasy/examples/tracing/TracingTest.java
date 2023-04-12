package dev.resteasy.examples.tracing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.UriBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;
import org.jboss.resteasy.tracing.RESTEasyTracingLogger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@ExtendWith(ArquillianExtension.class)
@RunAsClient
public class TracingTest {

    @ArquillianResource
    private URI uri;

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, TracingTest.class.getSimpleName() + ".war")
                .addClasses(TracingApp.class, TracingConfigResource.class)
                .addAsWebInfResource(new StringAsset("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<web-app xmlns=\"https://jakarta.ee/xml/ns/jakartaee\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd\"\n"
                        +
                        "         version=\"5.0\">\n" +
                        "    <display-name>tracing-example</display-name>\n" +
                        "    <context-param>\n" +
                        "        <param-name>resteasy.server.tracing.type</param-name>\n" +
                        "        <param-value>ALL</param-value>\n" +
                        "    </context-param>\n" +
                        "    <context-param>\n" +
                        "        <param-name>resteasy.server.tracing.threshold</param-name>\n" +
                        "        <param-value>VERBOSE</param-value>\n" +
                        "    </context-param>\n" +
                        "</web-app>"), "web.xml");
    }

    @Test
    public void basicTest() {
        try (Client client = ClientBuilder.newClient()) {
            WebTarget target = client.target(uriBuilder().path("trace/type"));
            assertEquals(ResteasyContextParameters.RESTEASY_TRACING_TYPE_ALL, target.request().get(String.class));

            target = client.target(uriBuilder().path("trace/level"));
            assertEquals(ResteasyContextParameters.RESTEASY_TRACING_LEVEL_VERBOSE, target.request().get(String.class));

            target = client.target(uriBuilder().path("trace/logger"));
            assertEquals(RESTEasyTracingLogger.class.getName(), target.request().get(String.class));
        }
    }

    private UriBuilder uriBuilder() {
        return UriBuilder.fromUri(uri);
    }
}
