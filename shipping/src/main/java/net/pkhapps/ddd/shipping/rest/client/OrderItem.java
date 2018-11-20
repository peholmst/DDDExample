package net.pkhapps.ddd.shipping.rest.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shipping.domain.ProductId;

public class OrderItem {

    @JsonProperty("productId")
    private ProductId productId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("qty")
    private int quantity;

    OrderItem() {
    }

    public ProductId productId() {
        return productId;
    }

    public String description() {
        return description;
    }

    public int quantity() {
        return quantity;
    }
}
