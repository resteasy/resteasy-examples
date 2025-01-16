/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2025 Red Hat, Inc., and individual contributors
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
    window.addEventListener('load', () => {
        const form = document.forms.namedItem("form");

        const fileListing = document.querySelector("#file-listing");

        /**
         * Get a listing of all the files in the directory
         */
        const loadFiles = () => {
            fetch("api/upload/", {
                method: "GET",
            }).then(response => {
                if (response.status === 200) {
                    response.json().then((json) => {
                        for (const file of json) {
                            addFile(file);
                        }
                    });
                } else {
                    response.text().then(t => {
                        console.error("Failed to submit form", t);
                    });
                }
            }).catch(reason => {
                console.error(reason);
            });
        };

        /**
         * Add a file to the table
         * @param json the JSON representation of the file
         */
        const addFile = (json) => {
            const row = document.createElement("tr");
            const fileName = document.createElement("td");
            if (json.fileName) {
                const a = document.createElement("a");
                a.setAttribute("href", "api/upload/" + encodeURIComponent(json.fileName));
                a.textContent = json.fileName;
                fileName.appendChild(a);
            }
            row.appendChild(fileName);

            const mimeType = document.createElement("td");
            if (json.mimeType) {
                mimeType.textContent = json.mimeType;
            } else {
                mimeType.textContent = "N/A";
            }
            row.appendChild(mimeType);

            const size = document.createElement("td");
            if (json.size) {
                size.textContent = json.size;
            } else {
                size.textContent = "N/A";
            }
            row.appendChild(size);

            const permissions = document.createElement("td");
            if (json.permissions) {
                permissions.textContent = json.permissions;
            } else {
                permissions.textContent = "N/A";
            }
            row.appendChild(permissions);

            fileListing.append(row);
        };

        const currentTheme = document.querySelector("#current-theme");
        const darkThemeButton = document.querySelector("#dark-theme");
        const lightThemeButton = document.querySelector("#light-theme");

        darkThemeButton.addEventListener("click", () => {
            document.body.setAttribute("data-bs-theme", "dark");
            darkThemeButton.classList.add("active");
            lightThemeButton.classList.remove("active");
            currentTheme.classList.add("bi-moon-stars-fill");
            currentTheme.classList.remove("bi-sun-fill");
            // bi-sun-fill bi-moon-stars-fill
        });

        lightThemeButton.addEventListener("click", () => {
            document.body.setAttribute("data-bs-theme", "light");
            lightThemeButton.classList.add("active");
            darkThemeButton.classList.remove("active");
            currentTheme.classList.add("bi-sun-fill");
            currentTheme.classList.remove("bi-moon-stars-fill");
        });

        // Listen for the submit event
        form.addEventListener("submit", (event) => {
            event.preventDefault();
            const formData = new FormData(event.currentTarget);
            document.querySelector("#file").value = "";
            fetch("api/upload/", {
                method: "POST",
                body: formData
            }).then(response => {
                if (response.status === 200) {
                    response.json().then((json) => {
                        addFile(json);
                    });
                } else {
                    response.text().then(t => {
                        console.error("Failed to submit form", t);
                    });
                }
            }).catch(reason => {
                console.error(reason);
            });
        });

        // Load the default directory
        fetch("api/upload/dir", {
            method: "GET"
        }).then(response => {
            if (response.status === 200) {
                response.json().then((json) => {
                    document.querySelector("#location").textContent = json.dir;
                });
            } else {
                response.text().then(t => {
                    console.error("Failed to get default directory", t);
                });
            }
        }).catch(reason => {
            console.error(reason);
        });
        // Initial load of the currently listed files
        loadFiles();
    });
})()