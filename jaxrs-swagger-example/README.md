# jaxrs-swagger-sample

Sample project for integrate swagger with JAX-RS 

To generate json file for openAPI:

```shell script
$ mvn compile
```

The json file can be found in `target/swagger/jaxrs-api.json`, and it's content like: 

```json
{
  "openapi": "3.0.1",
  "info": {
    "title": "RESTEasy Swagger Example",
    "description": "JAX-RS api docs",
    "termsOfService": "https://example.com",
    "contact": {
      "email": "example@example.com"
    },
    "license": {
      "name": "Apache 2.0",
      "url": "http://www.apache.org/licenses/LICENSE-2.0.html"
    },
    "version": "1.0"
  },
  "paths": {
    "/dummy": {
      "get": {
        "tags": [
          "dummy"
        ],
        "summary": "get dummy",
        "description": "Get Dummy",
        "operationId": "get",
        "responses": {
          "200": {
            "description": "OK"
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "HttpWebResponse": {
        "type": "object",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "result": {
            "type": "object"
          },
          "message": {
            "type": "string"
          }
        }
      }
    }
  }
}
```

With this `jaxrs-api.json` file we can start a web service, to be convenient, we can use the official docker image of swagger.

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

