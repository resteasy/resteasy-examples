/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2022 Red Hat, Inc., and individual contributors
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

package dev.resteasy.examples.openapi;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OpenApiTestIT {
    @Test
    public void testResponse() {
        try (Client client = ClientBuilder.newClient()) {
            final WebTarget target = client.target("http://localhost:8080/openapi");
            try (Response response = target.request().get()) {
                Assertions.assertEquals(200, response.getStatus());
                final String doc = response.readEntity(String.class);
                Assertions.assertNotNull(doc);
                Assertions.assertTrue(doc.contains("title: ProductApplication"));
                Assertions.assertTrue(doc.contains("return a list of products"));
            }
        }
    }
}
