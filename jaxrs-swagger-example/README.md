# jaxrs-swagger-sample


Sample project for integrate swagger with jaxrs

To generate json file for openapi:

```shell script
$ mvn compile
```

The json file can be found in `target/swagger/jaxrs-api.json`, and it's content like: 

```json

```

With this `jaxrs-api.json` file we can start a web service, to be convenient, we can use the official docker image of swagger:
You may need to create a directory like `~/docker-share` and share it with docker first.

```shell script
# get image
$ docker pull swaggerapi/swagger-ui
# copy api data to share with docker
$ cp target/swagger/jaxrs-api.json ~/docker-share/swagger
# start a container 
$ docker run -itd -v ~/docker-share:/docker-share -e SWAGGER_JSON=/docker-share/swagger/jaxrs-api.json -p 8080:8080 swaggerapi/swagger-ui
```
Open your browser and navigate to http://localhost:8080, you can read your api in swagger form now.

