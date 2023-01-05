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

/**
 * A contact form.
 */
class ContactForm extends HTMLElement {
    constructor() {
        super();
    }

    get name() {
        return this.getAttribute("name");
    }

    get buttonId() {
        return this.getAttribute("data-button-id");
    }

    get buttonText() {
        return this.getAttribute("data-button-text");
    }

    get includeId() {
        return this.getAttribute("data-include-id");
    }

    connectedCallback() {
        const name = this.name;
        const buttonId = this.buttonId;
        const buttonText = this.buttonText;
        const form = document.createElement("form");
        form.setAttribute("name", name);
        // Use HTML for readability and ease of writing
        form.innerHTML = `
                    <div class="mb-3 input-group has-validation">
                        <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
                        <div class="form-floating">
                            <input id="${name}FirstName" pattern="[^0-9]*" type="text" class="form-control needs-validation"
                                   name="firstName">
                            <label for="${name}FirstName">First Name</label>
                        </div>
                        <div class="invalid-feedback">
                            Names cannot have numbers.
                        </div>
                    </div>
                    <div class="mb-3 input-group has-validation">
                        <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
                        <div class="form-floating">
                            <input id="${name}LastName" pattern="[^0-9]*" type="text" class="form-control needs-validation" name="lastName">
                            <label for="${name}LastName">Last Name:</label>
                        </div>
                        <div class="invalid-feedback">
                            Names cannot have numbers.
                        </div>
                    </div>
                    <div class="mb-3 input-group">
                        <span class="input-group-text"><i class="bi bi-building-fill"></i></span>
                        <div class="form-floating">
                            <input id="${name}CompanyName" type="text" class="form-control" name="companyName">
                            <label for="${name}CompanyName">Company:</label>
                        </div>
                    </div>
                    <div class="mb-3 input-group">
                        <span class="input-group-text"><i class="bi bi-phone-fill"></i></span>
                        <div class="form-floating">
                            <input id="${name}PhoneNumber" type="tel" class="form-control" name="phoneNumber">
                            <label for="${name}PhoneNumber">Phone Number:</label>
                        </div>
                    </div>
                    <div class="mb-3 input-group has-validation">
                        <span class="input-group-text"><i class="bi bi-envelope-fill"></i></span>
                        <div class="form-floating">
                            <input id="${name}Email" type="email" class="form-control needs-validation" name="email">
                            <label for="${name}Email">Email:</label>
                        </div>
                        <div class="invalid-feedback">
                            Invalid email address.
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary" id="${buttonId}">${buttonText}</button>
                    </div>
        `;
        // Do we need to add the input for the id?
        if (this.includeId) {
            const hiddenId = document.createElement("input");
            hiddenId.setAttribute("type", "hidden");
            hiddenId.setAttribute("name", "id");
            form.appendChild(hiddenId);
        }
        // Add listeners to each input requiring validation
        const inputs = form.querySelectorAll(".needs-validation");
        inputs.forEach((e) => {
            e.addEventListener("invalid", (event) => {
                const parent = event.target.parentElement;
                event.target.classList.add("is-invalid");
                // If there is a parent, it should simply be a div and the class needs to be added there too
                if (parent) {
                    parent.classList.add("is-invalid");
                }
            });
        });
        // Add a click listener to clear the is-invalid class
        const submitButton = form.querySelector("button[type='submit']");
        submitButton.addEventListener("click", () => {
            form.querySelectorAll(".is-invalid").forEach(e => e.classList.remove("is-invalid"));
        });
        this.appendChild(form);
    }
}

// Define the custom components
customElements.define("contact-form", ContactForm);