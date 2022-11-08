package dev.resteasy.examples.service;

import java.util.List;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import dev.resteasy.examples.data.Book;
import dev.resteasy.examples.data.BookListing;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("library")
public class Library {
    private final Map<Long, Book> books;

    public Library() {
        books = Map.ofEntries(
                Map.entry(9780596529260L, new Book("Leonard Richardson", 9780596529260L, "RESTful Web Services")),
                Map.entry(9780596009786L, new Book("Bill Burke", 9780596009786L, "EJB 3.0")),
                Map.entry(9780596158040L, new Book("Bill Burke", 9780596158040L, "RESTful Java with JAX-RS")));
    }

    @GET
    @Path("books")
    @Produces(MediaType.APPLICATION_JSON)
    public BookListing getBooksMapped() {
        return new BookListing(List.copyOf(books.values()));
    }

    @GET
    @Path("books/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") final long isbn) {
        final Book book = books.get(isbn);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }

}
