package net.pkhapps.ddd.invoicing.application;

import net.pkhapps.ddd.invoicing.domain.model.Invoice;
import net.pkhapps.ddd.invoicing.domain.model.InvoiceRepository;
import net.pkhapps.ddd.invoicing.domain.model.OrderProcessedEvent;
import net.pkhapps.ddd.invoicing.rest.client.Order;
import net.pkhapps.ddd.invoicing.rest.client.OrderClient;
import net.pkhapps.ddd.shared.domain.geo.Address;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Clock;

@Service
class InvoiceCreator {

    private static final int DEFAULT_TERMS = 14;
    private final InvoiceRepository invoiceRepository;
    private final OrderClient orderClient;
    private final Clock clock;

    InvoiceCreator(InvoiceRepository invoiceRepository, OrderClient orderClient, Clock clock) {
        this.invoiceRepository = invoiceRepository;
        this.orderClient = orderClient;
        this.clock = clock;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOrderProcessedEvent(OrderProcessedEvent orderProcessedEvent) {
        var orderId = orderProcessedEvent.orderId();
        // Check if we have done this before (events can receive many times in case of errors)
        if (invoiceRepository.findByOrderId(orderId).count() == 0) {
            orderClient.findById(orderId).map(this::createInvoice).ifPresent(invoiceRepository::save);
        }
    }

    @NonNull
    private Invoice createInvoice(@NonNull Order order) {
        var billingAddress = order.billingAddress();
        var invoice = new Invoice(clock.instant(), order.orderId(), DEFAULT_TERMS, billingAddress.name(),
                new Address(billingAddress.address1(),
                        billingAddress.address2(),
                        billingAddress.cityName(),
                        billingAddress.postalCode(),
                        billingAddress.country()),
                order.currency());
        order.items().forEach(item -> invoice.addItem(item.description(), item.price(), item.vat(), item.qty()));
        return invoice;
    }
}
