# Demo Of Undertow Embedded Spring Container  

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

Using the `curl` command to access this URL:

```bash
$ curl http://localhost:8081/rest/foo
```

It will return `Hello, world!` from server. And the message is produced by Spring bean `HelloWorldBean` injected into JAX-RS resource class `FooResource`.

