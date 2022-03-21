package com.restfully.shop.domain;

import jakarta.ws.rs.core.Link;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@XmlRootElement(name = "customers")
public class Customers
{
   protected Collection<Customer> customers;
   protected List<Link> links;

   @XmlElementRef
   public Collection<Customer> getCustomers()
   {
      return customers;
   }

   public void setCustomers(Collection<Customer> customers)
   {
      this.customers = customers;
   }

   @XmlElement(name="link")
   @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
   public List<Link> getLinks()
   {
      return links;
   }

   public void setLinks(List<Link> links)
   {
      this.links = links;
   }

   @XmlTransient
   public URI getNext()
   {
      if (links == null) return null;
      for (Link link : links)
      {
         if ("next".equals(link.getRel())) return link.getUri();
      }
      return null;
   }

   @XmlTransient
   public URI getPrevious()
   {
      if (links == null) return null;
      for (Link link : links)
      {
         if ("previous".equals(link.getRel())) return link.getUri();
      }
      return null;
   }

}
