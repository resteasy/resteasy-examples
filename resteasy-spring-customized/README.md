Spring and Resteasy
===================
Example of using RESTEasy with:
- Spring
- Jetty (embedded)

The module shows an example to fully customize the Spring and RESTEasy loading process.

Building the project:
-------------------------

```bash
$ mvn clean install
```

Running the project and manually testing it:
-------------------------

```bash
$ mvn jetty:run
```

Using the `curl` command to access this URL:

```bash
$ curl localhost:8080/rest/servlet
```

And it will print out the internal `Dispatcher` instance fetched from `SpringBeanProcessorServletAware`.
