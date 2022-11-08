package dev.resteasy.examples.data;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeDeserializer;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@JsonbTypeDeserializer(BookListingDeserializer.class)
public class BookListing {
    private final Set<Book> books;

    @JsonbCreator
    public BookListing(@JsonbProperty Collection<Book> books) {
        this.books = new TreeSet<>(books);
    }

    public Set<Book> getBooks() {
        return books;
    }

}
