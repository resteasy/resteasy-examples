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

package dev.resteasy.quickstart.bootstrap;

import java.util.concurrent.TimeUnit;

import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class GreeterTestCase {

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
    public void greet() {
        try (Client client = ClientBuilder.newClient()) {
            final String name = System.getProperty("user.name", "RESTEasy");
            final Response response = client.target(INSTANCE.configuration().baseUriBuilder().path("/rest/" + name))
                    .request()
                    .get();
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> String.format("Expected 200 got %d: %s", response.getStatus(), response.readEntity(String.class)));
            Assertions.assertEquals(String.format("Hello %s!", name), response.readEntity(String.class));
        }
    }
}
