package net.pkhapps.ddd.invoicing.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.invoicing.domain.model.OrderId;
import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

class OrderStateChangedEvent implements DomainEvent {

    private final Instant occurredOn;
    private final OrderId orderId;
    private final String state;

    @JsonCreator
    public OrderStateChangedEvent(@NonNull @JsonProperty("occurredOn") Instant occurredOn,
                                  @NonNull @JsonProperty("orderId") OrderId orderId,
                                  @NonNull @JsonProperty("state") String state) {
        this.occurredOn = Objects.requireNonNull(occurredOn, "occurredOn must not be null");
        this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
        this.state = Objects.requireNonNull(state, "state must not be null");
    }

    @Override
    @NonNull
    public Instant occurredOn() {
        return occurredOn;
    }

    @NonNull
    public OrderId orderId() {
        return orderId;
    }

    @NonNull
    public String state() {
        return state;
    }
}
