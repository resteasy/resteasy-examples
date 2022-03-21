package com.restfully.shop.features;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ContentMD5Writer implements WriterInterceptor
{
   @Override
   public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException
   {
      MessageDigest digest = null;
      try
      {
         digest = MessageDigest.getInstance("MD5");
      }
      catch (NoSuchAlgorithmException e)
      {
         throw new IllegalArgumentException(e);
      }
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      DigestOutputStream digestStream = new DigestOutputStream(buffer, digest);
      OutputStream old = context.getOutputStream();
      context.setOutputStream(digestStream);

      try
      {
         context.proceed();

         byte[] hash = digest.digest();
         String encodedHash = Base64.getEncoder().encodeToString(hash);
         context.getHeaders().putSingle("Content-MD5", encodedHash);

         byte[] content = buffer.toByteArray();
         old.write(content);
      }
      finally
      {
         context.setOutputStream(old);
      }
   }
}
