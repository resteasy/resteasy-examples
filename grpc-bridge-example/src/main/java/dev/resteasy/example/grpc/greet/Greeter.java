package dev.resteasy.example.grpc.greet;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("")
public class Greeter {

    @GET
    @Path("greet/{s}")
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting greet(@PathParam("s") String s) {
        return new Greeting("hello, " + s);
    }

    @GET
    @Path("salute/{s}")
    @Produces(MediaType.APPLICATION_JSON)
    public GeneralGreeting generalGreet(@QueryParam("salute") String salute, @PathParam("s") String s) {
        return getGeneralGreeting(salute, s);
    }

    private GeneralGreeting getGeneralGreeting(String salute, String name) {
        return new GeneralGreeting(salute, name);
    }
}
