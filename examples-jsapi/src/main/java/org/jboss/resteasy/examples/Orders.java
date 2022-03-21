package org.jboss.resteasy.examples;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@Path("orders")
public class Orders {

	@Path("{id}")
	@GET
	@Produces("text/plain")
	public String getOrder(@PathParam("id") String id) {
		return "Order Id: " + id;
	}

}
