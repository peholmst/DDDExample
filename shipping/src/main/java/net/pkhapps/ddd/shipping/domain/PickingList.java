package net.pkhapps.ddd.shipping.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.AbstractAggregateRoot;
import net.pkhapps.ddd.shared.domain.base.ConcurrencySafeDomainObject;
import net.pkhapps.ddd.shared.domain.geo.Address;
import org.springframework.lang.NonNull;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "picking_lists")
public class PickingList extends AbstractAggregateRoot<PickingListId> implements ConcurrencySafeDomainObject {

    @Version
    private Long version;
    @Column(name = "order_id", nullable = false, unique = true)
    private OrderId orderId;
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;
    @Embedded
    private Address recipientAddress;
    @Column(name = "picking_list_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private PickingListState state;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "picking_list_items")
    private Set<PickingListItem> items;

    @SuppressWarnings("unused") // Used by JPA only
    private PickingList() {
    }

    public PickingList(@Nonnull OrderId orderId, @Nonnull String recipientName, @Nonnull Address recipientAddress) {
        super(PickingListId.randomId(PickingListId.class));
        setOrderId(orderId);
        setRecipientName(recipientName);
        setRecipientAddress(recipientAddress);
        setState(PickingListState.WAITING);
        items = new HashSet<>();
    }

    @Nonnull
    @JsonProperty("orderId")
    public OrderId orderId() {
        return orderId;
    }

    private void setOrderId(@Nonnull OrderId orderId) {
        this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
    }

    @Nonnull
    @JsonProperty("recipientName")
    public String recipientName() {
        return recipientName;
    }

    private void setRecipientName(String recipientName) {
        this.recipientName = Objects.requireNonNull(recipientName, "recipientName must not be null");
    }

    @Nonnull
    @JsonProperty("recipientAddress")
    public Address recipientAddress() {
        return recipientAddress;
    }

    private void setRecipientAddress(@Nonnull Address recipientAddress) {
        this.recipientAddress = Objects.requireNonNull(recipientAddress, "recipientAddress must not be null");
    }

    @Nonnull
    @JsonProperty("state")
    public PickingListState state() {
        return state;
    }

    private void setState(@Nonnull PickingListState state) {
        this.state = Objects.requireNonNull(state, "state must not be null");
    }

    public void startAssembly() {
        if (state != PickingListState.WAITING) {
            throw new IllegalStateException("Cannot start assembly when state is " + state);
        }
        setState(PickingListState.ASSEMBLY);
    }

    public void ship() {
        if (state != PickingListState.ASSEMBLY) {
            throw new IllegalStateException("Cannot ship when state is " + state);
        }
        setState(PickingListState.SHIPPED);
    }

    @NonNull
    public PickingListItem addItem(@NonNull ProductId productId, String description, int qty) {
        var item = new PickingListItem(productId, description, qty);
        items.add(item);
        return item;
    }

    @NonNull
    @JsonProperty("items")
    public Stream<PickingListItem> items() {
        return items.stream();
    }

    @Override
    public Long version() {
        return version;
    }
}
