## Usage

This project provides a minimal example showing the usage of RESTEasy in a Servlet container like Jetty or Tomcat. 

To build the project:

```bash
$ mvn install
```

The example provides a Jetty plugin, so you can use the Maven command to start the server directly:

```bash
$ mvn jetty:run
```

After the Jetty server is started, you can access the service with the following command:


```bash
$ curl http://localhost:8080/app/greet
Hello, world!
```

Because Tomcat doesn't provide an updated Maven plugin, so to deploy the example into Tomcat, you need to download a Tomcat 11 manually, and then deploy the built war `servlet-example.war` into the Tomcat `webapp` directory. After the Tomcat server is started and the example is deployed, you can access the service like this:

```bash
➤ curl http://localhost:8080/servlet-example/app/greet
Hello, world!
```

