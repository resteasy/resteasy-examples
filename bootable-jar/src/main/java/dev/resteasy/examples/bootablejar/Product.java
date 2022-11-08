package dev.resteasy.examples.bootablejar;

import java.util.Objects;

public class Product implements Comparable<Product> {
    private String name;
    private int id;

    public Product() {
    }

    public Product(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Product)) {
            return false;
        }
        final Product other = (Product) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return "Product[id=" + id + ", name=" + name + "]";
    }

    @Override
    public int compareTo(final Product o) {
        return Integer.compare(id, o.id);
    }
}
