# Sample Project For Swagger And JAX-RS Integration

Here is the process to run the example.

The first step is to compile the project and generate the Swagger JSON file:

```bash
$ mvn compile
```

The above command will compile the project and generate the Swagger JSON file:

```bash
$ ls target/swagger
jaxrs-api.json
```

The next step is to run the Swagger UI server. The easiest way is to use the official Swagger UI docker container:

* [swagger-ui](https://hub.docker.com/r/swaggerapi/swagger-ui/)

You need to install Docker in your machine, and then run the following command to fetch the image and start the container:

```bash
$ docker run -itd -v $(pwd)/target/swagger:/swagger -e SWAGGER_JSON=/swagger/jaxrs-api.json -p 8888:8080 swaggerapi/swagger-ui
```

After the container is started, the Swagger UI server can be accessed at port `8888`:

```bash
$ curl localhost:8888 | head

<!-- HTML for static distribution bundle build -->
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Swagger UI</title>
    <link rel="stylesheet" type="text/css" href="./swagger-ui.css" >
    <link rel="icon" type="image/png" href="./favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="./favicon-16x16.png" sizes="16x16" />
    <style>
```

The next step is to start the service Jetty server:

```bash
$ mvn jetty:run
...
[INFO] Started ServerConnector@46c44cce{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
...
[INFO] Started Jetty Server
```

So the Swagger UI service is run at port `8888`, and the JAX-RS service is run at port `8080`.

