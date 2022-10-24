package dev.resteasy.examples;

import java.net.URI;
import java.util.Set;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import dev.resteasy.examples.data.Book;
import dev.resteasy.examples.data.BookListing;
import dev.resteasy.examples.data.BookListingDeserializer;
import dev.resteasy.examples.service.Library;
import dev.resteasy.examples.service.LibraryApplication;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@ExtendWith(ArquillianExtension.class)
@RunAsClient
public class JsonBindingTest {

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, JsonBindingTest.class.getSimpleName() + ".war")
                .addClasses(Book.class, BookListing.class, BookListingDeserializer.class, Library.class,
                        LibraryApplication.class);
    }

    @ArquillianResource
    private URI uri;

    @Test
    public void books() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(UriBuilder.fromUri(uri).path("library/books"))
                        .request(MediaType.APPLICATION_JSON).get()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> response.readEntity(String.class));
            final BookListing bookListing = response.readEntity(BookListing.class);
            Assertions.assertNotNull(bookListing);
            // We should have 3 books
            final Set<Book> books = bookListing.getBooks();
            Assertions.assertEquals(3, books.size(),
                    () -> "Expected 3 books, but got " + books);
            // The books should be in the ISBN order
            final Book[] array = books.toArray(new Book[0]);
            Assertions.assertEquals(9780596009786L, array[0].getISBN());
            Assertions.assertEquals(9780596158040L, array[1].getISBN());
            Assertions.assertEquals(9780596529260L, array[2].getISBN());
        }
    }

    @Test
    public void singleBook() {
        try (
                Client client = ClientBuilder.newClient();
                Response response = client.target(UriBuilder.fromUri(uri).path("library/books/9780596158040"))
                        .request(MediaType.APPLICATION_JSON).get()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(),
                    () -> response.readEntity(String.class));
            final Book book = response.readEntity(Book.class);
            Assertions.assertNotNull(book);
            Assertions.assertEquals(9780596158040L, book.getISBN());
            Assertions.assertEquals("Bill Burke", book.getAuthor());
            Assertions.assertEquals("RESTful Java with JAX-RS", book.getTitle());
        }
    }

}
