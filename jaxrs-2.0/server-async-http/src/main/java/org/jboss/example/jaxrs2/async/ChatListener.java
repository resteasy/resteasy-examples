package org.jboss.example.jaxrs2.async;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/listener")
public class ChatListener {

   public ChatListener(List<AsyncResponse> listeners)
   {
      this.listeners = listeners;
   }

   List<AsyncResponse> listeners;

   @GET
   @Path("ping")
   @Produces("text/plain")
   public String ping()
   {
      return "PONG";
   }

   @GET
   public void listen(@Suspended AsyncResponse res) {
      System.out.println("******* LISTENING *************");
      res.setTimeout(100, TimeUnit.SECONDS);
      synchronized(listeners) { listeners.add(res); }
   }
}
