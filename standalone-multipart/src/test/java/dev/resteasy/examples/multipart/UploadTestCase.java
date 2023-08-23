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

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class UploadTestCase {

    private static SeBootstrap.Instance INSTANCE;

    @BeforeAll
    public static void startInstance() throws Exception {
        INSTANCE = SeBootstrap.start(RestActivator.class)
                .toCompletableFuture().get(10, TimeUnit.SECONDS);
        Assertions.assertNotNull(INSTANCE, "Failed to start instance");
    }

    @AfterAll
    public static void stopInstance() throws Exception {
        if (INSTANCE != null) {
            INSTANCE.stop()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);
        }
    }

    @Test
    public void upload() throws Exception {
        try (Client client = ClientBuilder.newClient()) {
            final List<EntityPart> multipart = List.of(
                    EntityPart.withName("name")
                            .content("RESTEasy")
                            .mediaType(MediaType.TEXT_PLAIN_TYPE)
                            .build(),
                    EntityPart.withName("data")
                            .content("test content".getBytes(StandardCharsets.UTF_8))
                            .mediaType(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                            .build(),
                    EntityPart.withName("entity")
                            .content("entity-data")
                            .mediaType(MediaType.TEXT_PLAIN_TYPE)
                            .build());
            try (
                    Response response = client.target(INSTANCE.configuration().baseUriBuilder().path("api/upload"))
                            .request(MediaType.APPLICATION_JSON)
                            .post(Entity.entity(new GenericEntity<>(multipart) {
                            }, MediaType.MULTIPART_FORM_DATA))) {
                Assertions.assertEquals(Response.Status.OK, response.getStatusInfo());
            }
        }
    }
}
