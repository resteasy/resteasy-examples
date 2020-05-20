package com.acme.swagger.rest;

import com.acme.swagger.common.HttpWebResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public HttpWebResponse get() {
        return new HttpWebResponse<>(true, "dummy");
    }
}

