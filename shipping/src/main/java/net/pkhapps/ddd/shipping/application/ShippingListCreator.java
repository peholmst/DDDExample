package net.pkhapps.ddd.shipping.application;

import net.pkhapps.ddd.shared.domain.geo.Address;
import net.pkhapps.ddd.shipping.domain.PickingList;
import net.pkhapps.ddd.shipping.domain.PickingListRepository;
import net.pkhapps.ddd.shipping.integration.OrderCreatedEvent;
import net.pkhapps.ddd.shipping.rest.client.Order;
import net.pkhapps.ddd.shipping.rest.client.OrderCatalogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Nonnull;
import java.time.Clock;

@Service
class ShippingListCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingListCreator.class);

    private final OrderCatalogClient orderCatalogClient;
    private final PickingListRepository pickingListRepository;
    private final Clock clock;

    ShippingListCreator(OrderCatalogClient orderCatalogClient,
                        PickingListRepository pickingListRepository,
                        Clock clock) {
        this.orderCatalogClient = orderCatalogClient;
        this.pickingListRepository = pickingListRepository;
        this.clock = clock;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOrderCreatedEvent(OrderCreatedEvent event) {
        orderCatalogClient
                .findById(event.orderId())
                .map(this::createPickingList)
                .ifPresent(pickingListRepository::save);
    }

    @Nonnull
    private PickingList createPickingList(@Nonnull Order order) {
        LOGGER.info("Creating picking list for order {}", order.orderId());
        var pickingList = new PickingList(clock.instant(), order.orderId(), order.shippingAddress().name(),
                new Address(order.shippingAddress().address1(),
                        order.shippingAddress().address2(),
                        order.shippingAddress().cityName(),
                        order.shippingAddress().postalCode(),
                        order.shippingAddress().country()));
        order.items().forEach(item -> pickingList.addItem(item.productId(), item.description(), item.quantity()));
        return pickingList;
    }
}
