## Usage

This is a demonstration of the resteasy tracing feature.

To run the example, it should be compiled firstly with the following command:

```bash
$ mvn compile
```

After compiling the example, the server for demonstration should be started firstly with the command:

```bash
$ mvn exec:java -Dexec.mainClass="org.jboss.resteasy.tracing.examples.Main"
...
Server started.
```

After server started, we can access the server and get the tracing info:

```bash
$ curl -i http://localhost:8081/level | head
```

And here is the sample output:

```txt
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
HTTP/1.1 200 OK    0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
10Connection: keep-alive
0X-RESTEasy-Tracing-000: org.jboss.resteasy.plugins.server.servlet.Servlet3AsyncHttpRequest@4e5b611d START       [ ---- /  ---- ms |  ---- %] baseUri=[http://localhost:8081/] requestUri=[http://localhost:8081/level] method=[GET] authScheme=[n/a] accept=*/* accept-encoding=n/a accept-charset=n/a accept-language=n/a content-type=n/a content-length=n/a
 X-RESTEasy-Tracing-001: org.jboss.resteasy.plugins.server.servlet.Servlet3AsyncHttpRequest@4e5b611d START       [ ---- /  0.69 ms |  ---- %] Other request headers: Accept=[*/*] Host=[localhost:8081] User-Agent=[curl/7.54.0]
 X-RESTEasy-Tracing-002: org.jboss.resteasy.plugins.server.servlet.Servlet3AsyncHttpRequest@4e5b611d PRE-MATCH   [ 0.04 /  5.27 ms |  0.00 %] PreMatchRequest summary: 0 filters
 ...
```

Above is the basic usage of the sample. And you can modify the logging properties file in the sample to tweak the output level.

 


