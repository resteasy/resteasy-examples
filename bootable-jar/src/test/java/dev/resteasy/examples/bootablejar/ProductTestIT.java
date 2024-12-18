package dev.resteasy.examples.bootablejar;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
@RunAsClient
public class ProductTestIT {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        // Dummy deployment
        return ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testResponse() {
        try (Client client = ClientBuilder.newClient()) {
            final WebTarget target = client.target("http://localhost:8080/products");
            try (Response response = target.request().get()) {
                Assertions.assertEquals(200, response.getStatus());
                final Product[] products = response.readEntity(Product[].class);
                Assertions.assertEquals(3, products.length);
                checkProduct(products[0], 111);
                checkProduct(products[1], 222);
                checkProduct(products[2], 333);
            }
        }
    }

    private void checkProduct(final Product product, final int expectedId) {
        Assertions.assertEquals(expectedId, product.getId(), () -> String.format("Expected %d got %s", expectedId, product));
    }
}
