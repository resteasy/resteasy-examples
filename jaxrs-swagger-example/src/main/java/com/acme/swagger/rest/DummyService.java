package com.acme.swagger.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/dummy")
@Produces(MediaType.APPLICATION_JSON)
public class DummyService {

    @GET
    @Operation(
            summary = "get dummy",
            description = "Get Dummy",
            tags = {"dummy"}
    )
    @ApiResponse(responseCode = "200", description = "OK")
    public String get() {
        return "dummy";
    }
}

