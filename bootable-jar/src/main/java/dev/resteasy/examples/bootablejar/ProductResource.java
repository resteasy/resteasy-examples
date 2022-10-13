package dev.resteasy.examples.bootablejar;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/products")
public class ProductResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Product[] getProducts() {
        return new Product[] { new Product(111, "JBoss EAP"), new Product(222, "RHEL"),
                new Product(333, "CentOS") };
    }
}
