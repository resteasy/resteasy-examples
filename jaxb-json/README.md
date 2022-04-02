Jettison Example
=====================
Example of using RestEasy with:
- Using JSON with Resteasy
- The JAXB/JSON Provider
- Using jakarta.ws.rs.core.Application
- Using the <context-param> resteasy.servlet.mapping.prefix
- Jetty (embedded)

System Requirements:
-------------------------
- Maven 2.0.9 or higher

Building the project:
====================

```bash
mvn clean install
```

Running the project and manually testing it:
-------------------------

```bash
mvn jetty:run
```

The data is JAXB annotated classes marshalled to JSON. Use the following `curl` command to access the json data:

```bash
$ curl http://localhost:9095/resteasy/library/books/mapped
```

The data is JAXB annotated classes marshalled to JSON using the Jettison Badger format:

```bash
$ curl http://localhost:9095/resteasy/library/books/badger
```



