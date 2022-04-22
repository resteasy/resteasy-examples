# Spring MVC and RESTEasy Integration Example

Example of using RestEasy with:

- Spring
- Jetty (embedded)

The module shows an example show the usage of RESTEasy Spring-MVC integration.

## Building the project

```bash
$ mvn clean install
```

## Running the project and manually testing it

```bash
$ mvn jetty:run
```
Open a browser at the following URL:

> http://localhost:8080/rest/contacts

This will give a web page to enter contacts.

Using the `curl` command to access this URL:

```bash
$ curl http://localhost:8080/rest/foo
```

It will fetch the value of context parameter `foo` defined in `web.xml`. This shows the injection of `ServletContext` by `@Context` annotation.


## Deploying The Project To WildFly

This example has embedded `wildfly-maven-plugin` embedded, so it can be deployed to a managed WildFly server by running the Maven command. Run the following command to build the example, download WildFly server, start the server and finish deployment automatically:

```bash
$ mvn wildfly:run
```

After the embedded WildFly server is downloaded and run, access the example service with following `curl` command:

```bash
$ curl http://localhost:8080/resteasy-spring-mvc/rest/foo
```

And it will print the output result:

```bash
bar
```
