package dev.resteasy.examples;

import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dev.resteasy.junit.extension.annotations.RequestPath;
import dev.resteasy.junit.extension.annotations.RestBootstrap;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@RestBootstrap(OrdersTest.TestApplication.class)
public class OrdersTest {

    @Test
    public void createOrder(@RequestPath("/orders/1") final WebTarget target) {
        final Response response = target.request().get();
        Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), () -> response.readEntity(String.class));
        final Order order = response.readEntity(Order.class);
        Assertions.assertEquals(1, order.getId());
    }

    @ApplicationPath("/")
    public static class TestApplication extends Application {
        @Override
        public Set<Class<?>> getClasses() {
            return Set.of(Orders.class);
        }
    }
}
