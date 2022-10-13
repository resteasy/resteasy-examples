package dev.resteasy.examples.asyncjob;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/resource")
@ApplicationScoped
public class AsyncResource {
    private final Map<Integer, String> postMessages = new ConcurrentHashMap<>();
    private final AtomicReference<String> putMessage = new AtomicReference<>();
    private final AtomicInteger post = new AtomicInteger(0);

    @Resource
    private ManagedExecutorService executor;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public void post(final String msg, @Suspended final AsyncResponse response) {
        submit(response, () -> {
            final int id = post.incrementAndGet();
            postMessages.put(id, msg);
            return Integer.toString(id);
        });
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void get(@Suspended final AsyncResponse response) {
        submit(response, () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                response.resume(e);
            }
            final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            postMessages.values().forEach(arrayBuilder::add);
            final JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("post", arrayBuilder);
            final String current = putMessage.get();
            if (current != null) {
                jsonBuilder.add("put", current);
            }
            return jsonBuilder.build();
        });
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public void getPostMessage(@PathParam("id") final int id, @Suspended final AsyncResponse response) {
        submit(response, () -> postMessages.get(id));
    }

    @GET
    @Path("current")
    @Produces(MediaType.TEXT_PLAIN)
    public void getPutMessage(@Suspended final AsyncResponse response) {
        submit(response, putMessage::get);
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public void put(final String msg, @Suspended final AsyncResponse response) {
        submit(response, () -> putMessage.getAndSet(msg));
    }

    private void submit(final AsyncResponse response, final Supplier<Object> resume) {
        response.setTimeout(10L, TimeUnit.SECONDS);
        executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                response.resume(e);
            }
            response.resume(resume.get());
        });
    }
}
