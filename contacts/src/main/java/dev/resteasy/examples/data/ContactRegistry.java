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

package dev.resteasy.examples.data;

import java.util.Collection;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

import dev.resteasy.examples.model.Contact;

/**
 * Manages the contact repository.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@RequestScoped
@Transactional
public class ContactRegistry {
    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    /**
     * Returns all the current contacts.
     *
     * @return a collection of the current contacts
     */
    public Collection<Contact> getContacts() {
        return em.createNamedQuery("findAll", Contact.class)
                .getResultList();
    }

    /**
     * Finds the contact given the id.
     *
     * @param id the contact id
     * @return the contact or {@code null} if not found
     */
    public Contact getContactById(final long id) {
        return em.find(Contact.class, id);
    }

    /**
     * Updates, or adds if missing, the contact.
     *
     * @param contact the contact to update
     * @return the updated contact
     */
    public Contact update(@NotNull final Contact contact) {
        return em.merge(contact);
    }

    /**
     * Adds a contact to the repository.
     *
     * @param contact the contact to add
     * @return the contact
     */
    public Contact add(@NotNull final Contact contact) {
        em.persist(contact);
        return contact;
    }

    /**
     * Deletes the contact, if it exists, from the repository.
     *
     * @param id the contact id
     * @return the deleted contact
     */
    public Contact delete(final long id) {
        final Contact contact = em.find(Contact.class, id);
        if (contact != null) {
            em.remove(contact);
        }
        return contact;
    }
}
