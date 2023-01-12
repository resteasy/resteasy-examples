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

package dev.resteasy.examples.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.FormParam;

import dev.resteasy.examples.resources.ContactListener;

/**
 * An entity which represents a contact.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@EntityListeners(ContactListener.class)
@NamedQuery(name = "findAll", query = "SELECT c FROM Contact c ORDER BY c.lastName ASC, c.firstName ASC")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name")
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    @FormParam("firstName")
    private String firstName;
    @Column(name = "last_name")
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    @FormParam("lastName")
    private String lastName;

    @Column(name = "company_name")
    @FormParam("companyName")
    private String companyName;

    @Column(name = "phone_number")
    @FormParam("phoneNumber")
    private String phoneNumber;

    @Column
    @Email
    @FormParam("email")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Contact)) {
            return false;
        }
        final Contact other = (Contact) obj;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + getId()
                + ", firstName=" + getFirstName()
                + ", lastName=" + getLastName()
                + ", companyName=" + getCompanyName()
                + ", phonNumner=" + getPhoneNumber()
                + ", email=" + getEmail()
                + "]";
    }
}
