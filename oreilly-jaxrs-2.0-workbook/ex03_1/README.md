Your first JAX_RS Client and Server
========================
This project is a simple example showing usage of @Path, @GET, PUT, POST, and @PathParam.  It uses pure streaming
output as well. 

System Requirements:
-------------------------
- Maven 3.0.4 or higher

Building the project:
-------------------------
1. In root directory

mvn clean install

This will build a WAR and run it with embedded Jetty

Run Using Embedded Jetty
-------------------------
mvn jetty:run

# Test the aplication with CURL
------------------------
1. make XML file

touch customer.xml

vim customer.xml

<customer id="100"><first-name>Bill</first-name><last-name>Burke</last-name><street>256 Clarendon Street</street><city>Boston</city><state>MA</state><zip>02115</zip><country>USA</country></customer>

(XML on the customer.xml has to be a one-liner)

2. test application
  
curl -vX POST -H "Content-Type: application/xml" -d "@customer.xml" localhost:8080/services/customers

curl -X GET localhost:8080/services/customers/1
