package org.jboss.example.jaxrs2.async;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.core.Response;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/speaker")
public class ChatSpeaker {

   public ChatSpeaker(List<AsyncResponse> listeners)
   {
      this.listeners = listeners;
   }

   List<AsyncResponse> listeners;

   @POST
   @Consumes("text/plain")
   public void speak(String speech) {
      System.out.println("******* SPEAKING *************");
      synchronized (listeners)
      {
         Iterator<AsyncResponse> it = listeners.iterator();
         while (it.hasNext())
         {
            it.next().resume(Response.ok(speech, "text/plain").build());
            it.remove();
         }
      }
   }
}
