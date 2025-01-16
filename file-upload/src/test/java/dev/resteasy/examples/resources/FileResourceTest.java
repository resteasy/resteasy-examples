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
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dev.resteasy.examples.Environment;
import dev.resteasy.examples.SizeUnit;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@ArquillianTest
@RunAsClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileResourceTest {

    @ArquillianResource
    private URI baseURI;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(
                        FileResource.class,
                        RestActivator.class,
                        Environment.class,
                        SizeUnit.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @Order(1)
    public void uploadFile() throws Exception {
        final Path file = createTestFile(100);
        try (Client client = ClientBuilder.newClient()) {
            // Create the entity parts for the request
            final List<EntityPart> multipart = List.of(
                    EntityPart.withName("file")
                            .content(Files.readAllBytes(file))
                            .fileName(file.getFileName().toString())
                            .mediaType(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                            .build());
            try (Response response = client.target(createUri("/api/upload")).request()
                    .post(Entity.entity(new GenericEntity<>(multipart) {
                    }, MediaType.MULTIPART_FORM_DATA_TYPE))) {
                Assertions.assertEquals(200, response.getStatus(),
                        () -> String.format("File upload failed: %s", response.readEntity(String.class)));
                final JsonObject json = response.readEntity(JsonObject.class);
                Assertions.assertEquals(file.getFileName().toString(), json.getString("fileName"),
                        () -> String.format("Expected file name of %s in %s", file.getFileName().toString(), json));
            }
        }
    }

    @Test
    @Order(2)
    public void downloadFile() throws Exception {
        final Path file = createTestFile(1000);
        try (Client client = ClientBuilder.newClient()) {
            // Create the entity parts for the request
            final List<EntityPart> multipart = List.of(
                    EntityPart.withName("file")
                            .content(Files.readAllBytes(file))
                            .fileName(file.getFileName().toString())
                            .mediaType(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                            .build());
            try (Response response = client.target(createUri("/api/upload")).request()
                    .post(Entity.entity(new GenericEntity<>(multipart) {
                    }, MediaType.MULTIPART_FORM_DATA_TYPE))) {
                Assertions.assertEquals(200, response.getStatus(),
                        () -> String.format("File upload failed: %s", response.readEntity(String.class)));
                final JsonObject json = response.readEntity(JsonObject.class);
                Assertions.assertEquals(file.getFileName().toString(), json.getString("fileName"),
                        () -> String.format("Expected file name of %s in %s", file.getFileName().toString(), json));
            }
            // The file has been uploaded, attempt to download it
            try (Response response = client
                    .target(createUri(
                            "/api/upload/" + URLEncoder.encode(file.getFileName().toString(), StandardCharsets.UTF_8)))
                    .request().get()) {
                Assertions.assertEquals(200, response.getStatus(),
                        () -> String.format("File download failed: %s", response.readEntity(String.class)));
                // Check that the download String matches the data from the file
                Assertions.assertEquals(Files.readString(file), response.readEntity(String.class));
            }
        }
    }

    @Test
    @Order(3)
    public void directoryListing() {
        try (Client client = ClientBuilder.newClient()) {
            try (Response response = client.target(createUri("/api/upload")).request().get()) {
                Assertions.assertEquals(200, response.getStatus(),
                        () -> String.format("File query failed: %s", response.readEntity(String.class)));
                // The download object should be a JSON array and there should be two entries
                final JsonArray json = response.readEntity(JsonArray.class);
                Assertions.assertEquals(2, json.size());
            }
        }
    }

    private URI createUri(final String path) {
        return (UriBuilder.fromUri(baseURI).path(path)).build();
    }

    private static Path createTestFile(final int len) throws IOException {
        final Path file = Files.createTempFile("file-upload-", ".txt");
        Files.writeString(file, ("a" + System.lineSeparator()).repeat(len));
        return file;
    }
}
