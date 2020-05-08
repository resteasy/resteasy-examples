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


