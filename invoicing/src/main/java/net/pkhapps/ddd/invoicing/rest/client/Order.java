package net.pkhapps.ddd.invoicing.rest.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.invoicing.domain.model.OrderId;
import net.pkhapps.ddd.shared.domain.financial.Currency;

import java.util.Set;
import java.util.stream.Stream;

public class Order {

    @JsonProperty("id")
    private OrderId id;

    @JsonProperty("currency")
    private Currency currency;

    @JsonProperty("billingAddress")
    private RecipientAddress billingAddress;

    @JsonProperty("items")
    private Set<OrderItem> items;

    Order() {
    }

    public OrderId orderId() {
        return id;
    }

    public Currency currency() {
        return currency;
    }

    public RecipientAddress billingAddress() {
        return billingAddress;
    }

    public Stream<OrderItem> items() {
        return items.stream();
    }
}
