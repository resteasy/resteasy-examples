# Spring and RESTEasy Customized Example

Example of using RESTEasy with:
- Spring
- Jetty (embedded)

The module shows an example to fully customize the Spring and RESTEasy loading process.

## Building the project

```bash
$ mvn clean install
```

## Running the project and manually testing it

```bash
$ mvn jetty:run
```

Using the `curl` command to access this URL:

```bash
$ curl localhost:8080/rest/servlet
```

And it will print out the internal `Dispatcher` instance fetched from `SpringBeanProcessorServletAware`.


## Deploying The Project To WildFly

This example has embedded `wildfly-maven-plugin` embedded, so it can be deployed to a managed WildFly server by running the Maven command. Run the following command to build the example, download WildFly server, start the server and finish deployment automatically:

```bash
$ mvn wildfly:run
```

After the embedded WildFly server is downloaded and run, access the example service with following `curl` command: 

```bash
$ curl http://localhost:8080/resteasy-spring-example-customized/rest/servlet
```

And it will print the result like:

```bash
org.jboss.resteasy.core.SynchronousDispatcher@bc35ba
```
