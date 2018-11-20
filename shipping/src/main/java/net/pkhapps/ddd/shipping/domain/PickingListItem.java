package net.pkhapps.ddd.shipping.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.ValueObject;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PickingListItem implements ValueObject {

    @Column(name = "product_id", nullable = false)
    private ProductId productId;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "qty", nullable = false)
    private int quantity;

    @SuppressWarnings("unused") // Used by JPA only
    private PickingListItem() {
    }

    PickingListItem(@Nonnull ProductId productId, @Nonnull String description, int quantity) {
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.quantity = quantity;
    }

    @Nonnull
    @JsonProperty("productId")
    public ProductId productId() {
        return productId;
    }

    @Nonnull
    @JsonProperty("description")
    public String description() {
        return description;
    }

    @JsonProperty("qty")
    public int quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PickingListItem that = (PickingListItem) o;
        return quantity == that.quantity &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, description, quantity);
    }
}
