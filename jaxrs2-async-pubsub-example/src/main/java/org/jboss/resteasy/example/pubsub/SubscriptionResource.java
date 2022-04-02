package org.jboss.resteasy.example.pubsub;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/")
public class SubscriptionResource
{
   protected ConcurrentHashMap<String, Subscriber> subscribers = new ConcurrentHashMap<String, Subscriber>();
   protected Executor executor;

   public SubscriptionResource()
   {
      executor = Executors.newSingleThreadExecutor();
   }

   @POST
   @Path("subscribers")
   public Response create(@Context UriInfo uriInfo, final @FormParam("name") String name)
   {
      if (name == null) throw new BadRequestException();
      Subscriber subscriber = new Subscriber(10);
      subscribers.putIfAbsent(name, subscriber);
      return Response.created(uriInfo.getRequestUriBuilder().path(name).build()).build();
   }

   @POST
   @Path("subscription")
   public void post(@Context HttpHeaders headers, final byte[] content)
   {
      final MediaType type = headers.getMediaType();
      executor.execute(new Runnable()
      {
         @Override
         public void run()
         {
            for (Subscriber subscriber : subscribers.values())
            {
               subscriber.post(type, content);
            }

         }
      });
   }

   @GET
   @Path("subscribers/{name}")
   public void longPoll(@PathParam("name") String name, final @Suspended AsyncResponse response)
   {
      final Subscriber subscriber = subscribers.get(name);
      if (subscriber == null) throw new NotFoundException();
      executor.execute(new Runnable()
      {
         @Override
         public void run()
         {
            subscriber.poll(response);
         }
      });
   }
}
