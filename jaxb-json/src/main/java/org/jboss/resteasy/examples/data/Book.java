package org.jboss.resteasy.examples.data;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@XmlRootElement(name = "book")
public class Book
{
   private String author;
   private String ISBN;
   private String title;

   public Book()
   {
   }

   public Book(String author, String ISBN, String title)
   {
      this.author = author;
      this.ISBN = ISBN;
      this.title = title;
   }

   @XmlElement
   public String getAuthor()
   {
      return author;
   }

   public void setAuthor(String author)
   {
      this.author = author;
   }

   @XmlElement
   public String getISBN()
   {
      return ISBN;
   }

   public void setISBN(String ISBN)
   {
      this.ISBN = ISBN;
   }

   @XmlAttribute
   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }
}