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

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

import org.jboss.logging.Logger;

import dev.resteasy.examples.model.Contact;

/**
 * A listener for changes of contact entities. Once an add, update or delete has been made a notification is sent to
 * registered subscribers.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@ApplicationScoped
@Path("/subscribe")
public class ContactListener {
    private static final Logger LOGGER = Logger.getLogger(ContactListener.class);

    @Inject
    private Sse sse;

    @Inject
    private SseBroadcaster broadcaster;

    @PreDestroy
    public void close() {
        broadcaster.close(true);
    }

    /**
     * Subscribes a client to the events of an entity.
     *
     * @param sink the event sink to register
     */
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void subscribe(@Context final SseEventSink sink) {
        broadcaster.register(sink);
    }

    /**
     * Sends a notification to the subscribed clients that an entity as been added.
     *
     * @param contact the contact that has been added
     */
    @PostPersist
    public void notifyAdded(final Contact contact) {
        notifyClient(contact, "contact.persist.added");
    }

    /**
     * Sends a notification to the subscribed clients that an entity as been updated.
     *
     * @param contact the contact that has been updated
     */
    @PostUpdate
    public void notifyUpdated(final Contact contact) {
        notifyClient(contact, "contact.persist.updated");
    }

    /**
     * Sends a notification to the subscribed clients that an entity as been deleted.
     *
     * @param contact the contact that has been deleted
     */
    @PostRemove
    public void notifyRemoved(final Contact contact) {
        notifyClient(contact, "contact.persist.removed");
    }

    private void notifyClient(final Contact contact, final String name) {
        broadcaster.broadcast(sse.newEventBuilder()
                .name(name)
                .data(contact)
                .mediaType(MediaType.APPLICATION_JSON_TYPE).build())
                .whenComplete((value, error) -> {
                    if (error != null) {
                        LOGGER.errorf(error, "Failed to notify clients of event %s: %s", name, value);
                    }
                });
    }
}
