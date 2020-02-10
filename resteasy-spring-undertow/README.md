# Spring and RESTEasy Undertow Embedded Server

Example of using RESTEasy with:

- Spring
- Undertow

The module shows an example show the usage of RESTEasy Spring-MVC integration.

## Building the project

```bash
$ mvn clean install
```

## Running the project and manually testing it

```bash
$ mvn exec:java -Dexec.mainClass="org.jboss.resteasy.examples.springundertow.Main"
```

Open a browser at the following URL:

> http://localhost:8080/rest/contacts

This will give a web page to enter contacts.

Using the `curl` command to access this URL:

```bash
$ curl http://localhost:8080/rest/foo
```

It will fetch the value of context parameter `foo` defined in `web.xml`. This shows the injection of `ServletContext` by `@Context` annotation.
