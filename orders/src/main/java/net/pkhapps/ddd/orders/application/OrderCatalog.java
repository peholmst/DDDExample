package net.pkhapps.ddd.orders.application;

import net.pkhapps.ddd.orders.application.form.OrderForm;
import net.pkhapps.ddd.orders.application.form.RecipientAddressForm;
import net.pkhapps.ddd.orders.domain.model.Order;
import net.pkhapps.ddd.orders.domain.model.OrderId;
import net.pkhapps.ddd.orders.domain.model.OrderRepository;
import net.pkhapps.ddd.orders.domain.model.RecipientAddress;
import net.pkhapps.ddd.orders.domain.model.event.OrderCreated;
import net.pkhapps.ddd.shared.domain.financial.CurrencyConverter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OrderCatalog {

    private final Validator validator;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Clock clock;
    private final CurrencyConverter currencyConverter;

    OrderCatalog(Validator validator,
                 OrderRepository orderRepository,
                 ApplicationEventPublisher applicationEventPublisher,
                 Clock clock,
                 CurrencyConverter currencyConverter) {
        this.validator = validator;
        this.orderRepository = orderRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.clock = clock;
        this.currencyConverter = currencyConverter;
    }

    @NonNull
    public OrderId createOrder(@NonNull OrderForm form) {
        Objects.requireNonNull(form, "form must not be null");
        var constraintViolations = validator.validate(form);
        if (constraintViolations.size() > 0) {
            throw new ConstraintViolationException("The OrderForm is not valid", constraintViolations);
        }
        var order = orderRepository.saveAndFlush(toDomainModel(form));
        applicationEventPublisher.publishEvent(new OrderCreated(order.id(), order.orderedOn()));
        return order.id();
    }

    @NonNull
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @NonNull
    public Optional<Order> findById(@NonNull OrderId orderId) {
        Objects.requireNonNull(orderId, "orderId must not be null");
        return orderRepository.findById(orderId);
    }

    @NonNull
    private Order toDomainModel(@NonNull OrderForm orderForm) {
        var order = new Order(clock.instant(), orderForm.getCurrency(),
                toDomainModel(orderForm.getBillingAddress()),
                toDomainModel(orderForm.getShippingAddress()));
        orderForm.getItems().forEach(item -> order.addItem(item.getProduct(), item.getQuantity(), currencyConverter));
        return order;
    }

    @NonNull
    private RecipientAddress toDomainModel(@NonNull RecipientAddressForm form) {
        return new RecipientAddress(form.getName(), form.getAddressLine1(), form.getAddressLine2(), form.getCity(),
                form.getPostalCode(), form.getCountry());
    }
}
