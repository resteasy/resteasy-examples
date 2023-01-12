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

import java.net.URI;
import java.util.Collection;
import java.util.Objects;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import dev.resteasy.examples.data.ContactRegistry;
import dev.resteasy.examples.model.Contact;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@ExtendWith(ArquillianExtension.class)
@RunAsClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContactResourceTest {

    private static final Contact DEFAULT_CONTACT = new Contact();
    static {
        DEFAULT_CONTACT.setFirstName("Jane");
        DEFAULT_CONTACT.setLastName("Doe");
        DEFAULT_CONTACT.setEmail("jdoe@redhat.com");
        DEFAULT_CONTACT.setCompanyName("Red Hat, Inc.");
        DEFAULT_CONTACT.setPhoneNumber("8887334281");
    }

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, ContactResourceTest.class.getSimpleName() + ".war")
                .addClasses(
                        ContactRegistry.class,
                        Contact.class,
                        ContactListener.class,
                        ContactResource.class,
                        Producers.class,
                        RestActivator.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URI uri;

    @Test
    @Order(1)
    public void addContact() {
        try (
                Client client = ClientBuilder.newClient();
                Response createdResponse = client.target(UriBuilder.fromUri(uri).path("api/contact/add")).request()
                        .post(Entity.json(DEFAULT_CONTACT))) {
            Assertions.assertEquals(Response.Status.CREATED, createdResponse.getStatusInfo(),
                    () -> String.format("Invalid status: %s", createdResponse.readEntity(String.class)));
            // We should have the location
            try (Response response = client.target(createdResponse.getLocation()).request().get()) {
                Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                        () -> String.format("Invalid status: %s - %s", createdResponse.readEntity(String.class),
                                createdResponse.getLocation()));
                final Contact resolvedContact = response.readEntity(Contact.class);
                // Set the ID
                DEFAULT_CONTACT.setId(resolvedContact.getId());
                contactsArqEqual(resolvedContact);
            }
        }
    }

    @Test
    @Order(2)
    public void singleContact() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(UriBuilder.fromUri(uri).path("api/contact/"))
                        .request(MediaType.APPLICATION_JSON)
                        .get()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> String.format("Invalid status: %s", response.readEntity(String.class)));
            final Collection<Contact> contacts = response.readEntity(new GenericType<>() {
            });
            Assertions.assertEquals(1, contacts.size(),
                    () -> String.format("Expected a single contact, but got %s", contacts));
            contactsArqEqual(contacts.iterator().next());
        }
    }

    @Test
    @Order(3)
    public void getContact() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(UriBuilder.fromUri(uri).path("api/contact/1"))
                        .request(MediaType.APPLICATION_JSON)
                        .get()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> String.format("Invalid status: %s", response.readEntity(String.class)));
            final Contact resolvedContact = response.readEntity(Contact.class);
            contactsArqEqual(resolvedContact);
        }
    }

    @Test
    @Order(4)
    public void editContact() {
        DEFAULT_CONTACT.setFirstName("Changed");
        DEFAULT_CONTACT.setLastName("Another");
        try (
                Client client = ClientBuilder.newClient();
                Response createdResponse = client.target(UriBuilder.fromUri(uri).path("api/contact/edit"))
                        .request(MediaType.APPLICATION_JSON)
                        .put(Entity.json(DEFAULT_CONTACT))) {
            Assertions.assertEquals(Response.Status.CREATED, createdResponse.getStatusInfo(),
                    () -> String.format("Invalid status: %s", createdResponse.readEntity(String.class)));
            // We should have the location
            try (Response response = client.target(createdResponse.getLocation()).request().get()) {
                Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                        () -> String.format("Invalid status: %s - %s", createdResponse.readEntity(String.class),
                                createdResponse.getLocation()));
                final Contact resolvedContact = response.readEntity(Contact.class);
                contactsArqEqual(resolvedContact);
            }
        }
    }

    @Test
    @Order(5)
    public void deleteContact() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(UriBuilder.fromUri(uri).path("api/contact/delete/1"))
                        .request(MediaType.APPLICATION_JSON)
                        .delete()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> String.format("Invalid status: %s", response.readEntity(String.class)));
            final Contact resolvedContact = response.readEntity(Contact.class);
            contactsArqEqual(resolvedContact);
        }
    }

    @Test
    @Order(6)
    public void emptyContacts() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(UriBuilder.fromUri(uri).path("api/contact/"))
                        .request(MediaType.APPLICATION_JSON)
                        .get()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> String.format("Invalid status: %s", response.readEntity(String.class)));
            final Collection<Contact> contacts = response.readEntity(new GenericType<>() {
            });
            Assertions.assertTrue(contacts.isEmpty(),
                    () -> String.format("Expected an empty set of contacts, but got %s", contacts));
        }
    }

    private static void contactsArqEqual(final Contact secondary) {
        boolean equal = Objects.equals(DEFAULT_CONTACT.getId(), secondary.getId())
                && Objects.equals(DEFAULT_CONTACT.getFirstName(), secondary.getFirstName())
                && Objects.equals(DEFAULT_CONTACT.getLastName(), secondary.getLastName())
                && Objects.equals(DEFAULT_CONTACT.getCompanyName(), secondary.getCompanyName())
                && Objects.equals(DEFAULT_CONTACT.getEmail(), secondary.getEmail())
                && Objects.equals(DEFAULT_CONTACT.getPhoneNumber(), secondary.getPhoneNumber());
        Assertions.assertTrue(equal,
                () -> String.format("Expected contact %s%nFound contact %s", DEFAULT_CONTACT, secondary));
    }
}
