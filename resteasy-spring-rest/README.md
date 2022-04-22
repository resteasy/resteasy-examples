# RESTEasy support of Spring REST annotations 

The module shows an example of using RESTEasy to support Spring REST annotations.

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
$ curl http://localhost:8080/spring
```

It will return `Spring is coming!` from server side. And the resource class from server side is using Spring REST annotations:

```java
@RestController
@RequestMapping("/spring")
public class SpringRestAnnotationResource {

    @GetMapping("/")
    public String get() {
        return "Spring is coming!";
    }
}
```

## Deploying The Project To WildFly

This example has embedded `wildfly-maven-plugin` embedded, so it can be deployed to a managed WildFly server by running the Maven command. Run the following command to build the example, download WildFly server, start the server and finish deployment automatically:

```bash
$ mvn wildfly:run
```

After the embedded WildFly server is downloaded and run, access the example service with following `curl` command:

```bash
$ curl http://localhost:8080/resteasy-spring-example-rest/spring
```

And it will print the output result:

```bash
Spring is coming!
```

