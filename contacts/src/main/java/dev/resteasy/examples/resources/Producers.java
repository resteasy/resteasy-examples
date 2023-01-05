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

package dev.resteasy.examples.resources;

import java.util.concurrent.atomic.AtomicReference;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@ApplicationScoped
public class Producers {

    private final AtomicReference<SseBroadcaster> broadcaster = new AtomicReference<>();

    @Inject
    private Sse sse;

    /**
     * Creates the broadcaster used for the application via injection.
     *
     * @return the broadcaster to use
     */
    @Produces
    @ApplicationScoped
    public SseBroadcaster broadcaster() {
        return broadcaster.updateAndGet(sseBroadcaster -> {
            SseBroadcaster result = sseBroadcaster;
            if (result == null) {
                result = sse.newBroadcaster();
            }
            return result;
        });
    }
}
