import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.tracing.RESTEasyTracingLogger;
import org.jboss.resteasy.tracing.examples.Demo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class BasicQueryTest {


    UndertowJaxrsServer server = null;

    @Before
    public void before() {
        server = Demo.buildServer();
    }

    @After
    public void after() {
        server.stop();
    }

    @Test
    public void basicTest() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/type");
        assertEquals(ResteasyContextParameters.RESTEASY_TRACING_TYPE_ALL, target.request().get(String.class));

        target = client.target("http://localhost:8081/level");
        assertEquals(ResteasyContextParameters.RESTEASY_TRACING_LEVEL_VERBOSE, target.request().get(String.class));

        target = client.target("http://localhost:8081/logger");
        assertEquals(RESTEasyTracingLogger.class.getName(), target.request().get(String.class));

        client.close();

    }
}
