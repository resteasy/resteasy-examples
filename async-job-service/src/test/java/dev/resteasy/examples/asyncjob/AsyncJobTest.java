package dev.resteasy.examples.asyncjob;

import java.net.URI;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@ExtendWith(ArquillianExtension.class)
@RunAsClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AsyncJobTest {

    @ArquillianResource
    private URI uri;

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, AsyncJobTest.class.getSimpleName() + ".war")
                .addClasses(AsyncResource.class, RestApplication.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @Order(1)
    public void post() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(createUriBuilder())
                        .request().post(Entity.entity("post message", MediaType.TEXT_PLAIN_TYPE))) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), () -> response.readEntity(String.class));
            Assertions.assertEquals(1, response.readEntity(Integer.class));
        }
    }

    @Test
    @Order(2)
    public void put() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(createUriBuilder())
                        .request().put(Entity.entity("put message", MediaType.TEXT_PLAIN_TYPE))) {
            Assertions.assertEquals(Response.Status.NO_CONTENT, response.getStatusInfo(),
                    () -> response.readEntity(String.class));
        }
    }

    @Test
    @Order(3)
    public void get() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(createUriBuilder())
                        .request(MediaType.APPLICATION_JSON).get()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> response.readEntity(String.class));
            final JsonObject json = response.readEntity(JsonObject.class);
            final JsonArray array = json.getJsonArray("post");
            Assertions.assertEquals(1, array.size(), () -> "Expect a single array entry got: " + array);
            Assertions.assertEquals("post message", array.getString(0));
            Assertions.assertEquals("put message", json.getString("put"));
        }
    }

    @Test
    @Order(4)
    public void getPushMessage() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(createUriBuilder().path("1"))
                        .request(MediaType.TEXT_PLAIN_TYPE).get()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> response.readEntity(String.class));
            final String msg = response.readEntity(String.class);
            Assertions.assertEquals("post message", msg);
        }
    }

    @Test
    @Order(5)
    public void getPutMessage() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(createUriBuilder().path("current"))
                        .request(MediaType.TEXT_PLAIN_TYPE).get()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> response.readEntity(String.class));
            final String msg = response.readEntity(String.class);
            Assertions.assertEquals("put message", msg);
        }
    }

    @Test
    @Order(6)
    public void addAndPut() {
        try (Client client = ClientBuilder.newClient()) {
            final WebTarget target = client.target(createUriBuilder());
            try (Response response = target.request().post(Entity.entity("new message", MediaType.TEXT_PLAIN_TYPE))) {
                Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), () -> response.readEntity(String.class));
                Assertions.assertEquals(2, response.readEntity(Integer.class));
            }
            try (
                    Response response = target.request()
                            .put(Entity.entity("changed put message", MediaType.TEXT_PLAIN_TYPE))) {
                Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                        () -> response.readEntity(String.class));
                Assertions.assertEquals("put message", response.readEntity(String.class));
            }
            try (Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get()) {
                Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                        () -> response.readEntity(String.class));
                final JsonObject json = response.readEntity(JsonObject.class);
                final JsonArray array = json.getJsonArray("post");
                Assertions.assertEquals(2, array.size(), () -> "Expect two array entries got: " + array);
                Assertions.assertEquals("post message", array.getString(0));
                Assertions.assertEquals("new message", array.getString(1));
                Assertions.assertEquals("changed put message", json.getString("put"));
            }
        }
    }

    private UriBuilder createUriBuilder() {
        return UriBuilder.fromUri(uri).path("resource");
    }
}
