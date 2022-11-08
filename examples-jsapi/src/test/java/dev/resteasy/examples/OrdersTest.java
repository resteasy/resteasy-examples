package dev.resteasy.examples;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class OrdersTest {

    private static SeBootstrap.Instance SERVER;

    @BeforeAll
    public static void startServer() throws Exception {
        SERVER = SeBootstrap.start(TestApplication.class).toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    @AfterAll
    public static void stopServer() throws Exception {
        if (SERVER != null) {
            SERVER.stop().toCompletableFuture().get(5, TimeUnit.SECONDS);
        }
    }

    @Test
    public void createOrder() {
        try (Client client = ClientBuilder.newClient()) {
            final Response response = client.target(SERVER.configuration().baseUriBuilder().path("/orders/1"))
                    .request().get();
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), () -> response.readEntity(String.class));
            final Order order = response.readEntity(Order.class);
            Assertions.assertEquals(1, order.getId());
        }
    }

    @ApplicationPath("/")
    public static class TestApplication extends Application {
        @Override
        public Set<Class<?>> getClasses() {
            return Set.of(Orders.class);
        }
    }
}
