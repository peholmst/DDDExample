package net.pkhapps.ddd.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.orders.domain.model.event.OrderStateChanged;
import net.pkhapps.ddd.shared.domain.base.AbstractAggregateRoot;
import net.pkhapps.ddd.shared.domain.base.ConcurrencySafeDomainObject;
import net.pkhapps.ddd.shared.domain.base.DomainObjectId;
import net.pkhapps.ddd.shared.domain.financial.Currency;
import net.pkhapps.ddd.shared.domain.financial.CurrencyConverter;
import net.pkhapps.ddd.shared.domain.financial.Money;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "orders")
public class Order extends AbstractAggregateRoot<OrderId> implements ConcurrencySafeDomainObject {

    @Version
    private Long version;
    @Column(name = "ordered_on", nullable = false)
    private Instant orderedOn;
    @Column(name = "order_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Column(name = "order_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_state_changes")
    private Set<OrderStateChange> stateChangeHistory;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "billing_name", nullable = false)),
            @AttributeOverride(name = "addressLine1", column = @Column(name = "billing_addr1", nullable = false)),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "billing_addr2")),
            @AttributeOverride(name = "city", column = @Column(name = "billing_city", nullable = false)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "billing_postal_code", nullable = false)),
            @AttributeOverride(name = "country", column = @Column(name = "billing_country", nullable = false))
    })
    private RecipientAddress billingAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "shipping_name", nullable = false)),
            @AttributeOverride(name = "addressLine1", column = @Column(name = "shipping_addr1", nullable = false)),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "shipping_addr2")),
            @AttributeOverride(name = "city", column = @Column(name = "shipping_city", nullable = false)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "shipping_postal_code", nullable = false)),
            @AttributeOverride(name = "country", column = @Column(name = "shipping_country", nullable = false))
    })
    private RecipientAddress shippingAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Set<OrderItem> items;

    @SuppressWarnings("unused") // Used by JPA only
    private Order() {
    }

    public Order(@NonNull Instant orderedOn, @NonNull Currency currency, @NonNull RecipientAddress billingAddress,
                 @NonNull RecipientAddress shippingAddress) {
        super(DomainObjectId.randomId(OrderId.class));
        this.stateChangeHistory = new HashSet<>();
        this.items = new HashSet<>();
        setOrderedOn(orderedOn);
        setCurrency(currency);
        setState(OrderState.RECEIVED, orderedOn);
        setBillingAddress(billingAddress);
        setShippingAddress(shippingAddress);
    }

    @NonNull
    @JsonProperty("currency")
    public Currency currency() {
        return currency;
    }

    private void setCurrency(@NonNull Currency currency) {
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
    }

    @NonNull
    @JsonProperty("orderedOn")
    public Instant orderedOn() {
        return orderedOn;
    }

    private void setOrderedOn(@NonNull Instant orderedOn) {
        this.orderedOn = Objects.requireNonNull(orderedOn, "orderedOn must not be null");
    }

    @NonNull
    @JsonProperty("state")
    public OrderState state() {
        return state;
    }

    private void setState(@NonNull OrderState state, @NonNull Clock clock) {
        Objects.requireNonNull(clock, "clock must not be null");
        setState(state, clock.instant());
    }

    private void setState(@NonNull OrderState state, @NonNull Instant changedOn) {
        Objects.requireNonNull(state, "state must not be null");
        Objects.requireNonNull(changedOn, "changedOn must not be null");
        if (stateChangeHistory.stream().anyMatch(stateChange -> stateChange.state().equals(state))) {
            throw new IllegalStateException("Order has already been in state " + state);
        }
        this.state = state;
        var stateChange = new OrderStateChange(changedOn, state);
        stateChangeHistory.add(stateChange);
        if (stateChangeHistory.size() > 1) { // Don't fire an event for the initial state
            registerEvent(new OrderStateChanged(id(), stateChange.state(), stateChange.changedOn()));
        }
    }

    @NonNull
    @JsonProperty("shippingAddress")
    public RecipientAddress shippingAddress() {
        return shippingAddress;
    }

    private void setShippingAddress(@NonNull RecipientAddress shippingAddress) {
        this.shippingAddress = Objects.requireNonNull(shippingAddress, "shippingAddress must not be null");
    }

    @NonNull
    @JsonProperty("billingAddress")
    public RecipientAddress billingAddress() {
        return billingAddress;
    }

    private void setBillingAddress(@NonNull RecipientAddress billingAddress) {
        this.billingAddress = Objects.requireNonNull(billingAddress, "billingAddress must not be null");
    }

    @NonNull
    public OrderItem addItem(@NonNull Product product, int qty, @NonNull CurrencyConverter currencyConverter) {
        Objects.requireNonNull(product, "product must not be null");
        Objects.requireNonNull(currencyConverter, "currencyConverter must not be null");
        var item = new OrderItem(product.id(), product.name(), currencyConverter.convert(product.price(), currency()),
                product.valueAddedTax());
        item.setQuantity(qty);
        items.add(item);
        return item;
    }

    @NonNull
    @JsonProperty("items")
    public Stream<OrderItem> items() {
        return items.stream();
    }

    @NonNull
    @JsonProperty("stateChangeHistory")
    public Stream<OrderStateChange> stateChangeHistory() {
        return stateChangeHistory.stream();
    }

    @NonNull
    @JsonProperty("totalExcludingVAT")
    public Money totalExcludingTax() {
        return items().map(OrderItem::subtotalExcludingTax).reduce(new Money(currency, 0), Money::add);
    }

    @NonNull
    @JsonProperty("totalVAT")
    public Money totalTax() {
        return items().map(OrderItem::subtotalTax).reduce(new Money(currency, 0), Money::add);
    }

    @NonNull
    @JsonProperty("totalIncludingVAT")
    public Money totalIncludingTax() {
        return items().map(OrderItem::subtotalIncludingTax).reduce(new Money(currency, 0), Money::add);
    }

    public void cancel(@NonNull Clock clock) {
        setState(OrderState.CANCELLED, clock);
    }

    public void startProcessing(@NonNull Clock clock) {
        setState(OrderState.PROCESSING, clock);
    }

    public void finishProcessing(@NonNull Clock clock) {
        setState(OrderState.PROCESSED, clock);
    }

    @Override
    @Nullable
    public Long version() {
        return version;
    }
}
