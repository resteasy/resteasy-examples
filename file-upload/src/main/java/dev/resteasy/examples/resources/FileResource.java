/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2025 Red Hat, Inc., and individual contributors
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

package dev.resteasy.examples.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import dev.resteasy.examples.Environment;
import dev.resteasy.examples.SizeUnit;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Path("/upload")
public class FileResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/dir")
    public Response dir() {
        return Response.ok(Json.createObjectBuilder().add("dir", Environment.uploadDirectory().toString()).build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray listDirectory() throws IOException {
        final JsonArrayBuilder builder = Json.createArrayBuilder();
        try (var paths = Files.walk(Environment.uploadDirectory())) {
            for (var path : paths.filter(p -> !p.equals(Environment.uploadDirectory())).sorted().collect(Collectors.toList())) {
                builder.add(toJson(path));
            }
        }
        return builder.build();
    }

    @GET
    @Path("{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("fileName") String fileName) throws IOException {
        final var file = Environment.uploadDirectory().resolve(fileName);
        if (Files.notExists(file)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final Response.ResponseBuilder builder = Response.ok(Files.newInputStream(file));
        builder.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        builder.header("Content-Length", Files.size(file));
        return Response.ok(Files.newInputStream(file)).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public JsonObject uploadFile(@FormParam("file") final EntityPart file) throws IOException {

        if (file.getFileName().isEmpty()) {
            throw new BadRequestException("No fle was uploaded.");
        }
        final String fileName = file.getFileName().get();
        if (fileName.isBlank()) {
            throw new BadRequestException("The file name could not be determined.");
        }

        // Copy the contents to a file
        final var uploadFile = Environment.uploadDirectory().resolve(fileName);
        Files.copy(file.getContent(), uploadFile, StandardCopyOption.REPLACE_EXISTING);
        return toJson(uploadFile);
    }

    private static JsonObject toJson(final java.nio.file.Path file) throws IOException {
        final String mimeType = Files.probeContentType(file);
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("fileName", Environment.uploadDirectory().relativize(file).toString());
        if (mimeType == null) {
            builder.addNull("mimeType");
        } else {
            builder.add("mimeType", mimeType);
        }

        builder.add("size", SizeUnit.toHumanReadable(Files.size(file)));
        Environment.resolvePermissions(file)
                .ifPresent(permissions -> builder.add("permissions", permissions));
        return builder.build();
    }
}
