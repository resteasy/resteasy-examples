package dev.resteasy.examples;

import java.math.BigDecimal;
import java.util.Random;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("orders")
public class Orders {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order createOrder(@PathParam("id") final int id) {
        return createRandomOrder(id);
    }

    private static Order createRandomOrder(final int id) {
        final Random random = new Random();
        final Order order = new Order();
        order.setId(id);
        order.setItems(nonZero(random, 30));
        final String total = String.format("%d.%02d", nonZero(random, 300), random.nextInt(99));
        order.setTotal(new BigDecimal(total));
        return order;
    }

    private static int nonZero(final Random random, final int bound) {
        return nonZero(random, bound, 0);
    }

    private static int nonZero(final Random random, final int bound, final int count) {
        final int result = random.nextInt(bound);
        if (result == 0) {
            if (count == 10) {
                return 1;
            }
            return nonZero(random, bound, count + 1);
        }
        return result;
    }
}
