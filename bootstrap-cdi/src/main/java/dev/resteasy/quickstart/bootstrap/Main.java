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

import jakarta.ws.rs.SeBootstrap;

/**
 * An entry point for starting a REST container
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class Main {
    private static final boolean USE_CONSOLE = System.console() != null;

    public static void main(final String[] args) throws Exception {
        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
        SeBootstrap.start(RestActivator.class)
                .thenAccept(instance -> {
                    instance.stopOnShutdown(stopResult -> print("Stopped container (%s)", stopResult.unwrap(Object.class)));
                    print("Container running at %s",
                            instance.configuration().baseUri());
                    print("Example: %s",
                            instance.configuration().baseUriBuilder().path("rest/" + System.getProperty("user.name")).build());
                    print("Send SIGKILL to shutdown container");
                });
        Thread.currentThread().join();
    }

    private static void print(final String fmt, final Object... args) {
        if (USE_CONSOLE) {
            System.console().format(fmt, args)
                    .printf("%n");
        } else {
            System.out.printf(fmt, args);
            System.out.println();
        }
    }

}
