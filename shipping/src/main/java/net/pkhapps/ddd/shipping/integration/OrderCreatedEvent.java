package net.pkhapps.ddd.shipping.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import net.pkhapps.ddd.shipping.domain.OrderId;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

public class OrderCreatedEvent implements DomainEvent {

    @JsonProperty("orderId")
    private final OrderId orderId;
    @JsonProperty("occurredOn")
    private final Instant occurredOn;

    @JsonCreator
    public OrderCreatedEvent(@JsonProperty("orderId") @NonNull OrderId orderId,
                             @JsonProperty("occurredOn") @NonNull Instant occurredOn) {
        this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
        this.occurredOn = Objects.requireNonNull(occurredOn, "occurredOn must not be null");
    }

    @NonNull
    public OrderId orderId() {
        return orderId;
    }

    @Override
    @NonNull
    public Instant occurredOn() {
        return occurredOn;
    }
}
