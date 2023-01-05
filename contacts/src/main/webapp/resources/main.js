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

(() => {
    "use strict";
    // Table sorting
    const cellValue = (tr, index) => {
        return tr.children[index].innerText || tr.children[index].textContent;
    };
    // Returns a function responsible for sorting a specific column index
    // (idx = columnIndex, asc = ascending order?).
    const comparer = (idx, asc) => {
        // This is used by the array.sort() function...
        return (a, b) => {
            // This is a transient function, that is called straight away.
            // It allows passing in different order of args, based on
            // the ascending/descending order.
            return function (v1, v2) {
                // sort based on a numeric or localeCompare, based on type...
                return (v1 !== '' && v2 !== '' && !isNaN(v1) && !isNaN(v2))
                    ? v1 - v2
                    : v1.toString().localeCompare(v2);
            }(cellValue(asc ? a : b, idx), cellValue(asc ? b : a, idx));
        };
    };
    const sortTable = (tbody) => {
        Array.from(tbody.querySelectorAll("tr"))
            .sort(comparer(0, true))
            .forEach((tr) => {
                tbody.appendChild(tr)
            });
    };

    window.addEventListener("load", () => {
        loadContacts();
        // Add focus to the forms when loaded
        {
            const form = document.forms.namedItem("editContact");
            const modalElement = document.querySelector("#editContact");
            modalElement.addEventListener("shown.bs.modal", () => {
                form.querySelector("input[name='firstName']").focus();
            });
        }
        {
            const form = document.forms.namedItem("addContact");
            const modalElement = document.querySelector("#addContactForm");
            modalElement.addEventListener("shown.bs.modal", () => {
                form.querySelector("input[name='firstName']").focus();
            });
        }
        // Listen for changes to contacts
        const eventSource = new EventSource("api/subscribe");
        eventSource.onerror = (err) => {
            console.error("EventSource failed:", err);
            eventSource.close();
        };
        const tbody = document.querySelector("tbody");
        eventSource.addEventListener("contact.persist.added", (event) => {
            const contact = JSON.parse(event.data);
            // Create a new table row
            const tr = tbody.insertRow();
            createContactRow(contact, tr);
            sortTable(tbody);
        });
        eventSource.addEventListener("contact.persist.updated", (event) => {
            const contact = JSON.parse(event.data);
            // Get the table row
            const tr = tbody.querySelector(`tr[data-contact-id='${contact.id}']`);
            if (tr) {
                while (tr.firstChild) {
                    tr.removeChild(tr.firstChild);
                }
                createContactRow(contact, tr);
                sortTable(tbody);
            } else {
                console.error("Could not find row with id ", contact.id);
                loadContacts();
            }
        });
        eventSource.addEventListener("contact.persist.removed", (event) => {
            const contact = JSON.parse(event.data);
            // Get the table row
            const tr = tbody.querySelector(`tr[data-contact-id='${contact.id}']`);
            if (tr) {
                tr.remove();
            } else {
                console.error("Could not find row with id ", contact.id);
                loadContacts();
            }
        });
        window.addEventListener("beforeunload", () => {
            eventSource.close();
        });

        // Configure default form buttons
        const addButton = document.querySelector("#addButton");
        addButton.addEventListener("click", (e) => {
            // Prevent the default submit action
            e.preventDefault();
            createContact();
        });
        const updateButton = document.querySelector("#updateButton");
        updateButton.addEventListener("click", (e) => {
            // Prevent the default submit action
            e.preventDefault();
            editContact();
        });
    });

    /**
     * Loads the contacts table
     */
    function loadContacts() {
        fetch("api/contact")
            .then((r) => {
                r.json().then((json) => {
                    const tbody = document.querySelector("tbody");
                    // Clear the table
                    while (tbody.firstChild) {
                        tbody.removeChild(tbody.firstChild);
                    }

                    // Create a new entry for each contact
                    for (let x in json) {
                        const contact = json[x];
                        const tr = tbody.insertRow();
                        createContactRow(contact, tr);
                    }
                    sortTable(tbody);
                });
            });
    }

    function createContactRow(contact, tr) {
        tr.setAttribute("data-contact-id", contact.id);
        let name;
        if (contact.companyName) {
            name = contact.firstName + " " + contact.lastName + " (" + contact.companyName + ")"
        } else {
            name = contact.firstName + " " + contact.lastName;
        }
        tr.insertCell().textContent = name;
        const phoneNumber = tr.insertCell();
        const phoneNumberLink = document.createElement("a");
        phoneNumberLink.setAttribute("href", `tel:${contact.phoneNumber}`);
        phoneNumberLink.textContent = contact.phoneNumber;
        phoneNumber.appendChild(phoneNumberLink);
        tr.insertCell().textContent = contact.email;

        // Get the button template
        const buttonTemplate = document.querySelector("#contactEditButtons");
        const buttons = buttonTemplate.content.cloneNode(true);

        // Get the edit button
        const editButton = buttons.querySelector("button[name='edit']");
        editButton.addEventListener("click", () => {
            loadContactDetail(contact.id, document.forms.namedItem("editContact"));
        });

        // Get the delete button
        const deleteButton = buttons.querySelector("button[name='delete']");
        const deleteItem = deleteButton.querySelector("i");
        deleteItem.setAttribute("data-contact-id", contact.id);
        const modal = document.querySelector("#confirmDelete");
        modal.addEventListener("show.bs.modal", event => {
            // Button that triggered the modal
            const button = event.relatedTarget;
            const contactId = button.getAttribute("data-contact-id");

            const deleteButton = document.querySelector("#deleteButton");
            deleteButton.onclick = () => {
                fetch(`api/contact/delete/${contactId}`, {
                    method: "DELETE"
                }).then(response => {
                    // Hide the modal
                    bootstrap.Modal.getInstance("#confirmDelete").hide();
                    if (response.status === 200) {
                        response.json().then((json) => {
                            success(`Successfully deleted ${json.firstName} ${json.lastName}`);
                        });
                    } else {
                        response.text().then(t => {
                            error(`Failed to delete contact. Reason: ${t}`);
                        });
                    }
                }).catch(reason => {
                    console.error(reason);
                });
            };
        });

        tr.insertCell().appendChild(buttons);
    }

    /**
     * Load a contact detail
     * @param id the contact id
     */
    function loadContactDetail(id, form) {
        fetch("api/contact/" + id).then(response => {
            response.json().then((json) => {
                for (const key in json) {
                    const input = form.querySelector(`input[name='${key}']`);
                    if (input) {
                        input.value = json[key];
                    }
                }
            });
        });
    }

    /**
     * Submits creating a contact
     */
    function createContact() {
        const form = document.forms.namedItem("addContact");
        if (form.checkValidity()) {
            const payload = createPayload(form);
            fetch("api/contact/add", {
                method: "POST", headers: {
                    "Content-Type": "application/json"
                }, body: payload
            }).then(response => {
                if (response.status === 201) {
                    const modal = bootstrap.Modal.getInstance(document.querySelector("#addContactForm"));
                    modal.hide();
                    form.reset();
                } else {
                    response.text().then(t => {
                        error(`Failed to create contact ${payload.firstName}. Reason: ${t}`);
                    });
                }
            }).catch(reason => {
                console.error(reason);
            });
        }
    }

    /**
     * Submits editing a contact
     */
    function editContact() {
        const form = document.forms.namedItem("editContact");
        const payload = createPayload(form);
        if (form.checkValidity()) {
            fetch("api/contact/edit", {
                method: "PUT", headers: {
                    "Content-Type": "application/json"
                }, body: payload
            }).then(response => {
                if (response.status === 201) {
                    const modal = bootstrap.Modal.getInstance(document.querySelector("#editContact"));
                    modal.hide();
                    form.reset();
                } else {
                    response.text().then(t => {
                        error(`Failed to create contact ${payload.firstName}. Reason: ${t}`);
                    });
                }
            }).catch(reason => {
                console.error(reason);
            });
        }
    }

    /**
     * Creates the payload for the REST call.
     * @param form the form to extract the data from
     * @returns {string} the form in JSON format
     */
    function createPayload(form) {
        const formData = new FormData(form);
        let json = {};
        formData.forEach((value, key) => json[key] = value);
        return JSON.stringify(json);
    }

    function success(message) {
        showAlert(message);
    }

    function error(message) {
        showAlert(message, "danger", "false");
    }

    function showAlert(message, type = "success", autoHide = "true") {
        const alertPlaceholder = document.querySelector("#liveAlertPlaceholder");
        const alert = document.querySelector("#alert").content.cloneNode(true).querySelector("div.toast");
        alert.setAttribute("data-bs-autohide", autoHide);
        alert.classList.add("text-bg-" + type);
        const body = alert.querySelector(".toast-body");
        body.textContent = message;
        alert.addEventListener("hidden.bs.toast", () => {
            alertPlaceholder.removeChild(alert);
        });
        const toast = new bootstrap.Toast(alert);
        toast.show();
        alertPlaceholder.append(alert);
    }
})()