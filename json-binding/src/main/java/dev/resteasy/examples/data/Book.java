package dev.resteasy.examples.data;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@JsonbPropertyOrder({
        "title",
        "author",
        "ISBN"
})
public class Book implements Comparable<Book> {
    private String author;
    private long ISBN;
    private String title;

    public Book() {
    }

    public Book(String author, long ISBN, String title) {
        this.author = author;
        this.ISBN = ISBN;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(final Book o) {
        return Long.compare(ISBN, o.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Long.hashCode(ISBN));
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Book)) {
            return false;
        }
        final Book other = (Book) obj;
        return ISBN == other.ISBN;
    }

    @Override
    public String toString() {
        return "Book[title=" + title + ", author=" + author + ", ISBN=" + ISBN + "]";
    }
}