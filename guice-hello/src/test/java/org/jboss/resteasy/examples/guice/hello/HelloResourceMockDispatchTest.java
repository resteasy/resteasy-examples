package org.jboss.resteasy.examples.guice.hello;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.guice.ModuleProcessor;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;

public class HelloResourceMockDispatchTest
{
   private static final Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();
   private static final Injector injector = Guice.createInjector(new HelloMockModule());
   private static final ModuleProcessor moduleProcessor = new ModuleProcessor(dispatcher.getRegistry(), dispatcher.getProviderFactory());

   @BeforeClass
   public static void setup()
   {
      moduleProcessor.processInjector(injector);
   }

   @Test
   public void test() throws Exception
   {
      final MockHttpRequest request = MockHttpRequest.get("/hello/world");
      final MockHttpResponse response = new MockHttpResponse();
      dispatcher.invoke(request, response);
      Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
      Assert.assertEquals("Hello world", response.getContentAsString());
   }
}
