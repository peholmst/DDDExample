package net.pkhapps.ddd.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;
import java.util.Objects;

@Embeddable
public class OrderStateChange implements ValueObject {

    @Column(name = "changed_on", nullable = false)
    private Instant changedOn;
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @SuppressWarnings("unused") // Used by JPA only
    private OrderStateChange() {
    }

    OrderStateChange(@NonNull Instant changedOn, @NonNull OrderState state) {
        this.changedOn = Objects.requireNonNull(changedOn, "changedOn must not be null");
        this.state = Objects.requireNonNull(state, "state must not be null");
    }

    @NonNull
    @JsonProperty("changedOn")
    public Instant changedOn() {
        return changedOn;
    }

    @NonNull
    @JsonProperty("state")
    public OrderState state() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStateChange that = (OrderStateChange) o;
        return Objects.equals(changedOn, that.changedOn) &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(changedOn, state);
    }
}
