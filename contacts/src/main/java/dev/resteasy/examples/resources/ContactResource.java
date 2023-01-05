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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import dev.resteasy.examples.data.ContactRegistry;
import dev.resteasy.examples.model.Contact;

/**
 * A REST endpoint to manage contacts.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@RequestScoped
@Path("/contact")
public class ContactResource {

    @Inject
    private ContactRegistry contactRegistry;

    @Inject
    private UriInfo uriInfo;

    /**
     * Returns all current contacts.
     *
     * @return all the current contacts
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return Response.ok(contactRegistry.getContacts())
                .build();
    }

    /**
     * Returns the contact, if it exists, with the given id. If the contact does not exist a 404 is returned.
     *
     * @param id the contact id
     * @return the contact or a 404 if not found
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response get(@PathParam("id") final long id) {
        final Contact contact = contactRegistry.getContactById(id);
        return contact == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(contact).build();
    }

    /**
     * Updates the contact.
     *
     * @param contact the contact to update
     * @return the location to the updated contact
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/edit")
    public Response edit(@Valid final Contact contact) {
        return Response
                .created(uriInfo.getBaseUriBuilder().path("contact/" + contactRegistry.update(contact).getId()).build())
                .build();
    }

    /**
     * Adds the contact.
     *
     * @param contact the contact to add
     * @return the location to the added contact
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public Response add(@Valid final Contact contact) {
        return Response
                .created(uriInfo.getBaseUriBuilder().path("contact/" + contactRegistry.add(contact).getId()).build())
                .build();
    }

    /**
     * Deletes the contact with the given id.
     *
     * @param id the contact id
     * @return the deleted contact or a 404 if not found
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") final long id) {
        final Contact contact = contactRegistry.delete(id);
        return contact == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(contact).build();
    }
}
