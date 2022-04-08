mvn # Basic Spring and RESTEasy Integration Demo 

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

## Deploying The Project To WildFly

This example has embedded `wildfly-maven-plugin` embedded, so it can be deployed to a managed WildFly server by running the Maven command. Run the following command to build the example, download WildFly server, start the server and finish deployment automatically:

```bash
$ mvn wildfly:run
```

If everything goes fine you can see the WildFly server is downloaded and run, and the example is compiled and deployed to the running server:


```bash
[INFO] --- wildfly-maven-plugin:2.1.0.Final:run (default-cli) @ examples-resteasy-spring-basic ---
Downloading from central: https://repo.maven.apache.org/maven2/org/wildfly/wildfly-preview-dist/26.0.1.Final/wildfly-preview-dist-26.0.1.Final.zip
Downloaded from central: https://repo.maven.apache.org/maven2/org/wildfly/wildfly-preview-dist/26.0.1.Final/wildfly-preview-dist-26.0.1.Final.zip (226 MB at 275 kB/s)
[INFO] JAVA_HOME : /Users/weli/.sdkman/candidates/java/17.0.2-open
[INFO] JBOSS_HOME: /Users/weli/works/resteasy-examples/resteasy-spring-basic/target/wildfly-preview-26.0.1.Final
[INFO] JAVA_OPTS : -Xms64m -Xmx512m -Djava.net.preferIPv4Stack=true -Djava.awt.headless=true -Djboss.modules.system.pkgs=org.jboss.byteman
[INFO] Server is starting up. Press CTRL + C to stop the server.
[INFO] JBoss Threads version 2.4.0.Final
[INFO] JBoss Remoting version 5.0.23.Final
[INFO] XNIO version 3.8.4.Final
[INFO] XNIO NIO Implementation Version 3.8.4.Final
[INFO] ELY00001: WildFly Elytron version 1.17.1.Final
23:26:03,136 INFO  [org.jboss.modules] (main) JBoss Modules version 2.0.0.Final
23:26:04,392 INFO  [org.jboss.msc] (main) JBoss MSC version 1.4.13.Final
23:26:04,415 INFO  [org.jboss.threads] (main) JBoss Threads version 2.4.0.Final
23:26:04,704 INFO  [org.jboss.as] (MSC service thread 1-2) WFLYSRV0049: WildFly Preview 26.0.1.Final (WildFly Core 18.0.4.Final) starting
23:26:06,984 INFO  [org.wildfly.security] (ServerService Thread Pool -- 4) ELY00001: WildFly Elytron version 1.18.3.Final
23:26:08,758 INFO  [org.jboss.as.server] (Controller Boot Thread) WFLYSRV0039: Creating http management service using socket-binding (management-http)
23:26:08,807 INFO  [org.xnio] (MSC service thread 1-2) XNIO version 3.8.5.Final
23:26:08,821 INFO  [org.xnio.nio] (MSC service thread 1-2) XNIO NIO Implementation Version 3.8.5.Final
23:26:08,955 INFO  [org.wildfly.extension.elytron.oidc._private] (ServerService Thread Pool -- 52) WFLYOIDC0001: Activating WildFly Elytron OIDC Subsystem
23:26:09,101 INFO  [org.jboss.as.connector.subsystems.datasources] (ServerService Thread Pool -- 44) WFLYJCA0004: Deploying JDBC-compliant driver class org.h2.Driver (version 1.4)
23:26:09,104 WARN  [org.wildfly.extension.elytron] (MSC service thread 1-1) WFLYELY00023: KeyStore file '/Users/weli/works/resteasy-examples/resteasy-spring-basic/target/wildfly-preview-26.0.1.Final/standalone/configuration/application.keystore' does not exist. Used blank.
23:26:09,133 INFO  [org.wildfly.extension.health] (ServerService Thread Pool -- 53) WFLYHEALTH0001: Activating Base Health Subsystem
23:26:09,134 INFO  [org.jboss.as.clustering.infinispan] (ServerService Thread Pool -- 54) WFLYCLINF0001: Activating Infinispan subsystem.
23:26:09,156 INFO  [org.wildfly.extension.microprofile.opentracing] (ServerService Thread Pool -- 66) WFLYTRACEXT0001: Activating MicroProfile OpenTracing Subsystem
23:26:09,158 WARN  [org.wildfly.extension.elytron] (MSC service thread 1-5) WFLYELY01084: KeyStore /Users/weli/works/resteasy-examples/resteasy-spring-basic/target/wildfly-preview-26.0.1.Final/standalone/configuration/application.keystore not found, it will be auto generated on first use with a self-signed certificate for host localhost
23:26:09,162 INFO  [org.wildfly.extension.microprofile.config.smallrye] (ServerService Thread Pool -- 64) WFLYCONF0001: Activating MicroProfile Config Subsystem
23:26:09,167 WARN  [org.jboss.as.txn] (ServerService Thread Pool -- 74) WFLYTX0013: The node-identifier attribute on the /subsystem=transactions is set to the default value. This is a danger for environments running multiple servers. Please make sure the attribute value is unique.
23:26:09,189 INFO  [org.wildfly.extension.metrics] (ServerService Thread Pool -- 63) WFLYMETRICS0001: Activating Base Metrics Subsystem
23:26:09,216 INFO  [org.wildfly.extension.microprofile.jwt.smallrye] (ServerService Thread Pool -- 65) WFLYJWT0001: Activating MicroProfile JWT Subsystem
23:26:09,235 INFO  [org.jboss.as.jaxrs] (ServerService Thread Pool -- 56) WFLYRS0016: RESTEasy version 6.0.0.Beta1
23:26:09,235 INFO  [org.wildfly.extension.io] (ServerService Thread Pool -- 55) WFLYIO001: Worker 'default' has auto-configured to 8 IO threads with 64 max task threads based on your 4 available processors
23:26:09,246 INFO  [org.jboss.as.webservices] (ServerService Thread Pool -- 76) WFLYWS0002: Activating WebServices Extension
23:26:09,259 INFO  [org.jboss.as.jsf] (ServerService Thread Pool -- 61) WFLYJSF0007: Activated the following Jakarta Server Faces Implementations: [main]
23:26:09,267 INFO  [org.jboss.remoting] (MSC service thread 1-2) JBoss Remoting version 5.0.23.Final
23:26:09,329 INFO  [org.jboss.as.naming] (ServerService Thread Pool -- 67) WFLYNAM0001: Activating Naming Subsystem
23:26:09,435 INFO  [org.jboss.as.connector] (MSC service thread 1-7) WFLYJCA0009: Starting Jakarta Connectors Subsystem (WildFly/IronJacamar 1.5.3.Final)
23:26:09,436 INFO  [org.jboss.as.naming] (MSC service thread 1-6) WFLYNAM0003: Starting Naming Service
23:26:09,437 INFO  [org.jboss.as.connector.deployers.jdbc] (MSC service thread 1-7) WFLYJCA0018: Started Driver service with driver-name = h2
23:26:09,438 INFO  [org.jboss.as.mail.extension] (MSC service thread 1-7) WFLYMAIL0001: Bound mail session [java:jboss/mail/Default]
23:26:09,770 INFO  [org.jboss.as.ejb3] (MSC service thread 1-1) WFLYEJB0482: Strict pool mdb-strict-max-pool is using a max instance size of 16 (per class), which is derived from the number of CPUs on this host.
23:26:09,770 INFO  [org.jboss.as.ejb3] (MSC service thread 1-5) WFLYEJB0481: Strict pool slsb-strict-max-pool is using a max instance size of 64 (per class), which is derived from thread worker pool sizing.
23:26:09,786 INFO  [org.wildfly.extension.undertow] (MSC service thread 1-3) WFLYUT0003: Undertow 2.2.14.Final starting
23:26:09,971 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 75) WFLYUT0014: Creating file handler for path '/Users/weli/works/resteasy-examples/resteasy-spring-basic/target/wildfly-preview-26.0.1.Final/welcome-content' with options [directory-listing: 'false', follow-symlink: 'false', case-sensitive: 'true', safe-symlink-paths: '[]']
23:26:09,972 INFO  [org.wildfly.extension.undertow] (MSC service thread 1-3) WFLYUT0012: Started server default-server.
23:26:09,973 INFO  [org.wildfly.extension.undertow] (MSC service thread 1-6) Queuing requests.
23:26:09,975 INFO  [org.wildfly.extension.undertow] (MSC service thread 1-6) WFLYUT0018: Host default-host starting
23:26:10,217 INFO  [org.wildfly.extension.undertow] (MSC service thread 1-5) WFLYUT0006: Undertow HTTP listener default listening on 127.0.0.1:8080
23:26:10,223 INFO  [org.jboss.as.ejb3] (MSC service thread 1-6) WFLYEJB0493: Jakarta Enterprise Beans subsystem suspension complete
23:26:10,467 INFO  [org.wildfly.extension.undertow] (MSC service thread 1-6) WFLYUT0006: Undertow HTTPS listener https listening on 127.0.0.1:8443
23:26:10,493 INFO  [org.jboss.as.connector.subsystems.datasources] (MSC service thread 1-8) WFLYJCA0001: Bound data source [java:jboss/datasources/ExampleDS]
23:26:11,697 INFO  [org.jboss.as.patching] (MSC service thread 1-3) WFLYPAT0050: WildFly Preview cumulative patch ID is: base, one-off patches include: none
23:26:11,704 INFO  [org.jboss.as.server.deployment.scanner] (MSC service thread 1-4) WFLYDS0013: Started FileSystemDeploymentService for directory /Users/weli/works/resteasy-examples/resteasy-spring-basic/target/wildfly-preview-26.0.1.Final/standalone/deployments
23:26:11,890 INFO  [org.jboss.ws.common.management] (MSC service thread 1-6) JBWS022052: Starting JBossWS 5.4.4.Final (Apache CXF 3.4.5) 
23:26:12,188 INFO  [org.jboss.as.server] (Controller Boot Thread) WFLYSRV0212: Resuming server
23:26:12,193 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Preview 26.0.1.Final (WildFly Core 18.0.4.Final) started in 10573ms - Started 298 of 538 services (337 services are lazy, passive or on-demand)
23:26:12,197 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0060: Http management interface listening on http://127.0.0.1:9990/management
23:26:12,197 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0051: Admin console listening on http://127.0.0.1:9990
23:26:30,210 INFO  [org.jboss.as.repository] (management-handler-thread - 1) WFLYDR0001: Content added at location /Users/weli/works/resteasy-examples/resteasy-spring-basic/target/wildfly-preview-26.0.1.Final/standalone/data/content/e1/98e70c574ff208da61e97d6972a06be15b80d4/content
23:26:30,305 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-6) WFLYSRV0027: Starting deployment of "resteasy-spring-example-basic.war" (runtime-name: "resteasy-spring-example-basic.war")
23:26:37,535 WARN  [org.jboss.as.ee] (MSC service thread 1-5) WFLYEE0007: Not installing optional component org.springframework.http.server.reactive.ServletServerHttpResponse$ResponseAsyncListener due to an exception (enable DEBUG log level to see the cause)
23:26:37,541 WARN  [org.jboss.as.ee] (MSC service thread 1-5) WFLYEE0007: Not installing optional component org.springframework.http.server.ServletServerHttpAsyncRequestControl due to an exception (enable DEBUG log level to see the cause)
23:26:37,547 WARN  [org.jboss.as.ee] (MSC service thread 1-5) WFLYEE0007: Not installing optional component org.springframework.web.context.request.async.StandardServletAsyncWebRequest due to an exception (enable DEBUG log level to see the cause)
23:26:37,553 WARN  [org.jboss.as.ee] (MSC service thread 1-5) WFLYEE0007: Not installing optional component org.springframework.http.server.reactive.ServletHttpHandlerAdapter$HttpHandlerAsyncListener due to an exception (enable DEBUG log level to see the cause)
23:26:37,561 WARN  [org.jboss.as.ee] (MSC service thread 1-5) WFLYEE0007: Not installing optional component org.springframework.http.server.reactive.ServletServerHttpRequest$RequestAsyncListener due to an exception (enable DEBUG log level to see the cause)
23:26:37,565 WARN  [org.jboss.as.ee] (MSC service thread 1-5) WFLYEE0007: Not installing optional component org.jboss.resteasy.plugins.server.servlet.Servlet3AsyncHttpRequest$Servlet3ExecutionContext$Servlet3AsynchronousResponse due to an exception (enable DEBUG log level to see the cause)
23:26:38,341 INFO  [org.infinispan.CONTAINER] (ServerService Thread Pool -- 78) ISPN000128: Infinispan version: Infinispan 'Taedonggang' 12.1.7.Final
23:26:38,471 INFO  [org.infinispan.CONFIG] (MSC service thread 1-5) ISPN000152: Passivation configured without an eviction policy being selected. Only manually evicted entities will be passivated.
23:26:38,473 INFO  [org.infinispan.CONFIG] (MSC service thread 1-5) ISPN000152: Passivation configured without an eviction policy being selected. Only manually evicted entities will be passivated.
23:26:38,570 INFO  [org.infinispan.CONTAINER] (ServerService Thread Pool -- 78) ISPN000556: Starting user marshaller 'org.wildfly.clustering.infinispan.spi.marshalling.InfinispanProtoStreamMarshaller'
23:26:38,776 INFO  [org.infinispan.CONTAINER] (ServerService Thread Pool -- 78) ISPN000025: wakeUpInterval is <= 0, not starting expired purge thread
23:26:38,843 INFO  [org.jboss.as.clustering.infinispan] (ServerService Thread Pool -- 78) WFLYCLINF0002: Started http-remoting-connector cache from ejb container
23:26:39,093 INFO  [io.undertow.servlet] (ServerService Thread Pool -- 98) No Spring WebApplicationInitializer types detected on classpath
23:26:40,267 INFO  [io.undertow.servlet] (ServerService Thread Pool -- 98) Initializing Spring root WebApplicationContext
23:26:40,273 INFO  [org.springframework.web.context.ContextLoader] (ServerService Thread Pool -- 98) Root WebApplicationContext: initialization started
23:26:42,757 INFO  [org.hibernate.validator.internal.util.Version] (ServerService Thread Pool -- 98) HV000001: Hibernate Validator 7.0.1.Final
23:26:42,910 INFO  [org.springframework.web.context.ContextLoader] (ServerService Thread Pool -- 98) Root WebApplicationContext initialized in 2594 ms
23:26:43,079 INFO  [jakarta.enterprise.resource.webcontainer.jsf.config] (ServerService Thread Pool -- 98) 初始化上下文 '/resteasy-spring-example-basic' 的 Mojarra 3.0.0.SP04
23:26:46,270 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 98) WFLYUT0021: Registered web context: '/resteasy-spring-example-basic' for server 'default-server'
23:26:46,376 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "resteasy-spring-example-basic.war" (runtime-name : "resteasy-spring-example-basic.war")
```

Please not you need to have Jave 17 installed to run this, because the Spring 6 build requires the 17 version of Java.

And now you can access all the services provided by the example. The only difference is that you need to add the `/resteasy-spring-example-basic` part into your URL address. For example, to access the `/hello` service said above, run the following command:

```bash
$ curl http://localhost:8080/resteasy-spring-example-basic/rest/foo/hello
```

And it should return the result provided by Wildfly and the example service:

```bash
Hello, world!
```

Enjoy!



