package com.restfully.shop.services;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Provider
@Produces("application/example-java")
@Consumes("application/example-java")
public class JavaMarshaller implements MessageBodyReader, MessageBodyWriter
{
   public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType)
   {
      return Serializable.class.isAssignableFrom(type);
   }

   public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType,
                          MultivaluedMap httpHeaders, InputStream is) throws IOException, WebApplicationException
   {
      ObjectInputStream ois = new ObjectInputStream(is);
      try
      {
         return ois.readObject();
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType)
   {
      return Serializable.class.isAssignableFrom(type);
   }

   public long getSize(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType)
   {
      return -1;
   }

   public void writeTo(Object o, Class type, Type genericType, Annotation[] annotations,
                       MediaType mediaType, MultivaluedMap httpHeaders, OutputStream os) throws IOException, WebApplicationException
   {
      ObjectOutputStream oos = new ObjectOutputStream(os);
      oos.writeObject(o);
   }
}
