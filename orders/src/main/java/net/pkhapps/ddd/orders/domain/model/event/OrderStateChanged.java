package net.pkhapps.ddd.orders.domain.model.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.orders.domain.model.OrderId;
import net.pkhapps.ddd.orders.domain.model.OrderState;
import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

public class OrderStateChanged implements DomainEvent {

    @JsonProperty("orderId")
    private final OrderId orderId;
    @JsonProperty("state")
    private final OrderState state;
    @JsonProperty("occurredOn")
    private final Instant occurredOn;

    @JsonCreator
    public OrderStateChanged(@JsonProperty("orderId") @NonNull OrderId orderId,
                             @JsonProperty("state") @NonNull OrderState state,
                             @JsonProperty("occurredOn") @NonNull Instant occurredOn) {
        this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
        this.state = Objects.requireNonNull(state, "state must not be null");
        this.occurredOn = Objects.requireNonNull(occurredOn, "occurredOn must not be null");
    }

    @NonNull
    public OrderId orderId() {
        return orderId;
    }

    @NonNull
    public OrderState state() {
        return state;
    }

    @Override
    @NonNull
    public Instant occurredOn() {
        return occurredOn;
    }
}
