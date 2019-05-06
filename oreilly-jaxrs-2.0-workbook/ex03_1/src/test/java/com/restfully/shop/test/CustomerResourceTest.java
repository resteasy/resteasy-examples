package com.restfully.shop.test;

import com.restfully.shop.services.ShoppingApplication;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;


/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CustomerResourceTest
{

   UndertowJaxrsServer server = null;

   @Before
   public void before() {
      server = new UndertowJaxrsServer().start();

      ResteasyDeployment deployment = new ResteasyDeploymentImpl();
      deployment.setApplicationClass(ShoppingApplication.class.getName());

      DeploymentInfo di = server.undertowDeployment(deployment);
      di.setClassLoader(CustomerResourceTest.class.getClassLoader());
      di.setContextPath("");
      di.setDeploymentName("Resteasy");

      server.deploy(di);
   }

   @After
   public void after() {
      server.stop();
   }

   @Test
   @Ignore
   public void testCustomerResource() throws Exception
   {
      Client client = ClientBuilder.newClient();
      try {
         System.out.println("*** Create a new Customer ***");

         String xml = "<customer>"
                 + "<first-name>Bill</first-name>"
                 + "<last-name>Burke</last-name>"
                 + "<street>256 Clarendon Street</street>"
                 + "<city>Boston</city>"
                 + "<state>MA</state>"
                 + "<zip>02115</zip>"
                 + "<country>USA</country>"
                 + "</customer>";

         Response response = client.target("http://localhost:8081/customers")
                 .request().post(Entity.xml(xml));
         if (response.getStatus() != 201) throw new RuntimeException("Failed to create a new Customer.");
         String location = response.getLocation().toString();
         System.out.println("Location: " + location);
         response.close();

         System.out.println("*** GET Created Customer **");
         String customer = client.target(location).request().get(String.class);
         System.out.println(customer);

         String updateCustomer = "<customer>"
                 + "<first-name>William</first-name>"
                 + "<last-name>Burke</last-name>"
                 + "<street>256 Clarendon Street</street>"
                 + "<city>Boston</city>"
                 + "<state>MA</state>"
                 + "<zip>02115</zip>"
                 + "<country>USA</country>"
                 + "</customer>";
         response = client.target(location).request().put(Entity.xml(updateCustomer));
         if (response.getStatus() != 204) throw new RuntimeException("Failed to update");
         response.close();
         System.out.println("**** After Update ***");
         customer = client.target(location).request().get(String.class);
         System.out.println(customer);
      } finally {
         client.close();
      }
   }
}
