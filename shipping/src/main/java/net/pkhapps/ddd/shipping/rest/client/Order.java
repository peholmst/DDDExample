package net.pkhapps.ddd.shipping.rest.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shipping.domain.OrderId;

import java.util.List;
import java.util.stream.Stream;

public class Order {

    @JsonProperty("id")
    private OrderId orderId;
    @JsonProperty("state")
    private OrderState state;
    @JsonProperty("shippingAddress")
    private RecipientAddress shippingAddress;
    @JsonProperty("items")
    private List<OrderItem> items;

    Order() {
    }

    public OrderId orderId() {
        return orderId;
    }

    public OrderState state() {
        return state;
    }

    public RecipientAddress shippingAddress() {
        return shippingAddress;
    }

    public Stream<OrderItem> items() {
        return items.stream();
    }
}
