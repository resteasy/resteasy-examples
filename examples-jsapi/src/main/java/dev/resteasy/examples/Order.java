package dev.resteasy.examples;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class Order {
    private int id;
    private int items;

    private BigDecimal total;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getItems() {
        return items;
    }

    public void setItems(final int items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Order)) {
            return false;
        }
        return id == ((Order) obj).id;
    }

    @Override
    public String toString() {
        return "Order[id=" + id + ", items=" + items + ", total=" + total + "]";
    }
}
