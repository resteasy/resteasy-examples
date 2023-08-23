/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.resteasy.examples.multipart;

import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * A simple resource for creating a greeting.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Path("/")
public class UploadResource {

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormParam("name") final String name, @FormParam("data") final InputStream data,
            @FormParam("entity") final EntityPart entityPart) {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", name);

        // Read the data into a string
        try (data) {
            builder.add("data", new String(data.readAllBytes()));
        } catch (IOException e) {
            throw new ServerErrorException("Failed to read data " + data, Response.Status.BAD_REQUEST);
        }
        try {
            builder.add(entityPart.getName(), entityPart.getContent(String.class));
        } catch (IOException e) {
            throw new ServerErrorException("Failed to read entity " + entityPart, Response.Status.BAD_REQUEST);
        }
        return Response.ok(builder.build()).build();
    }
}
