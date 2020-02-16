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

