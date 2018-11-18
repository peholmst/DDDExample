package net.pkhapps.ddd.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.AbstractEntity;
import net.pkhapps.ddd.shared.domain.base.DomainObjectId;
import net.pkhapps.ddd.shared.domain.financial.Currency;
import net.pkhapps.ddd.shared.domain.financial.Money;
import net.pkhapps.ddd.shared.domain.financial.VAT;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem extends AbstractEntity<OrderItemId> {

    @Column(name = "product_id", nullable = false)
    private ProductId productId;
    @Column(name = "item_description", nullable = false)
    private String itemDescription;
    @Column(name = "item_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency itemPriceCurrency;
    @Column(name = "item_price", nullable = false)
    private int itemPrice;
    @Column(name = "value_added_tax", nullable = false)
    private VAT valueAddedTax;
    @Column(name = "qty", nullable = false)
    private int quantity;

    @SuppressWarnings("unused") // Used by JPA only
    private OrderItem() {
    }

    OrderItem(@NonNull ProductId productId, @NonNull String itemDescription, @NonNull Money itemPrice,
              @NonNull VAT valueAddedTax) {
        super(DomainObjectId.randomId(OrderItemId.class));
        setProductId(productId);
        setItemDescription(itemDescription);
        setItemPrice(itemPrice);
        setValueAddedTax(valueAddedTax);
    }

    @NonNull
    @JsonProperty("productId")
    public ProductId productId() {
        return productId;
    }

    private void setProductId(@NonNull ProductId productId) {
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
    }

    @NonNull
    @JsonProperty("description")
    public String itemDescription() {
        return itemDescription;
    }

    private void setItemDescription(@NonNull String itemDescription) {
        this.itemDescription = Objects.requireNonNull(itemDescription, "itemDescription must not be null");
    }

    @NonNull
    @JsonProperty("price")
    public Money itemPrice() {
        return Money.valueOf(itemPriceCurrency, itemPrice);
    }

    private void setItemPrice(@NonNull Money itemPrice) {
        Objects.requireNonNull(itemPrice, "itemPrice must not be null");
        this.itemPrice = itemPrice.fixedPointAmount();
        this.itemPriceCurrency = itemPrice.currency();
    }

    @NonNull
    @JsonProperty("valueAddedTax")
    public VAT valueAddedTax() {
        return valueAddedTax;
    }

    private void setValueAddedTax(@NonNull VAT valueAddedTax) {
        this.valueAddedTax = Objects.requireNonNull(valueAddedTax, "valueAddedTax must not be null");
    }

    @NonNull
    @JsonProperty("qty")
    public int quantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    @NonNull
    @JsonProperty("subtotalExcludingVAT")
    public Money subtotalExcludingTax() {
        return itemPrice().multiply(quantity());
    }

    @NonNull
    @JsonProperty("subtotalVAT")
    public Money subtotalTax() {
        return valueAddedTax().calculateTax(subtotalExcludingTax());
    }

    @NonNull
    @JsonProperty("subtotalIncludingVAT")
    public Money subtotalIncludingTax() {
        return valueAddedTax().addTax(subtotalExcludingTax());
    }
}
