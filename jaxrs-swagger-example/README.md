# jaxrs-swagger-sample


Sample project for integrate swagger with jaxrs

To generate json file for openapi:

```shell script
$ mvn compile
```

Start web service with docker image

```shell script
# get image
$ docker pull swaggerapi/swagger-ui
# copy api data to share with docker
$ cp target/swagger ~/docker-share/swagger
# start a container 
$ docker run -itd -v ~/docker-share:/docker-share -e SWAGGER_JSON=/docker-share/swagger/jaxrs-api.json -p 8081:8080 swaggerapi/swagger-ui
```

Related articles：

* [JAX-RS集成Swagger](https://blog.moicen.com/jaxrs-swagger)
* [RESTEasy Swagger Integration Report](https://gist.github.com/liweinan/670101827d317104e85b2579d12c5c42)

