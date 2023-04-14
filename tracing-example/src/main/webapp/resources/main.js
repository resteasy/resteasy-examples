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

    window.addEventListener("load", () => {
        // Enable tooltips
        const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
        [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl, {
            trigger: "hover",
            delay: 500
        }));
        // Add the functions to the buttons
        const submitButton = document.querySelector("#submit");
        submitButton.addEventListener("click", () => {
            const radioButtons = document.querySelectorAll("input[name='httpMethod']");
            let httpMethod;
            for (const radioButton of radioButtons) {
                if (radioButton.checked) {
                    httpMethod = radioButton.value;
                    break;
                }
            }
            loadTable(httpMethod);
        });
        const clearButton = document.querySelector("#clear");
        clearButton.addEventListener("click", () => {
            // Clear the elements we want to be reset
            const link = document.querySelector("#url");
            clearElement(link);
            link.classList.remove("placeholder", "col-5", "bg-secondary");
            clearElement(document.querySelector("#sentHttpMethod"));
            clearElement(document.querySelector("#response"));
            clearElement(document.querySelector("#output"));
        });
    });

    function loadTable(httpMethod) {
        const link = document.querySelector("#url");
        document.querySelector("#sentHttpMethod").textContent = httpMethod.toUpperCase();
        clearElement(link);
        link.classList.add("placeholder", "col-5", "bg-secondary");

        // Get the table body for the output
        const tbody = document.querySelector("#output");
        // Clear the table
        clearElement(tbody);
        // Add a temporary row to the table to indicate we are loading
        const trTemp = tbody.insertRow();
        const col1Temp = trTemp.insertCell();
        const spinnerDiv = document.createElement("div");
        spinnerDiv.classList.add("spinner-border");
        spinnerDiv.setAttribute("role", "status");
        const spinner = document.createElement("span");
        spinner.classList.add("visually-hidden");
        spinner.textContent = "Loading...";
        spinnerDiv.append(spinner);
        col1Temp.append(spinnerDiv);
        trTemp.insertCell();

        const url = "trace/headers/" + httpMethod;
        // Create potential data
        let config = {};
        if (httpMethod === "post" || httpMethod === "put") {
            config = {
                method: httpMethod.toUpperCase(),
                body: `${httpMethod} data`
            };
        }
        fetch(url, config)
            .then((r) => {
                // Create a link to the resource we just invoked
                const a = document.createElement("a");
                a.setAttribute("href", r.url);
                a.setAttribute("target", "_");
                a.textContent = r.url;
                link.classList.remove("placeholder", "col-5", "bg-secondary");
                link.append(a);
                r.text().then((text) => {
                    clearElement(tbody);
                    document.querySelector("#response").textContent = text;
                    // Add a table entry for each header value
                    const headers = r.headers;
                    for (let header of headers.entries()) {
                        const tr = tbody.insertRow();
                        const name = tr.insertCell();
                        name.textContent = header[0];
                        const value = tr.insertCell();
                        value.textContent = header[1];
                    }
                });
            }).catch(err => {
            error("Request has failed: " + err);
        });
    }

    /**
     * Clears the children of the element.
     * @param e the element to clear
     */
    function clearElement(e) {
        while (e.firstChild) {
            e.removeChild(e.firstChild);
        }
    }

    function success(message) {
        showAlert(message);
    }

    function error(message) {
        showAlert(message, "danger");
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