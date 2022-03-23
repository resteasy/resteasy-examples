package org.jboss.resteasy.bootablejar;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;

@Path("/products")
public class Jackson2Resource {
   @GET
   @Produces("application/json")
   public Jackson2Product[] getProducts() {

      Jackson2Product[] products = {new Jackson2Product(111, "JBoss EAP"), new Jackson2Product(222, "RHEL"), new Jackson2Product(333, "CentOS")};
      return products;
   }
}
