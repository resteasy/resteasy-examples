# Basic Spring and RESTEasy Integration Demo 

Example of using RESTEasy with:

- Spring
- Jetty (embedded)

The module shows an example to use RESTEasy's basic Spring Framework integration.

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
$ curl http://localhost:8080/rest/foo
```

It will fetch the value of context parameter `foo` defined in `web.xml`. This shows the injection of `ServletContext` by `@Context` annotation.

And using  the `curl` command to access another URL:

```bash
$ curl http://localhost:8080/rest/foo/hello
```

It will give the `Hello, world!` message provided by autowired bean `FooResource`.
