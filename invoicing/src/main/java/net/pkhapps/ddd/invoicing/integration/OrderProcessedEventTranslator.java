package net.pkhapps.ddd.invoicing.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.ddd.invoicing.domain.model.OrderProcessedEvent;
import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import net.pkhapps.ddd.shared.infra.eventlog.RemoteEventTranslator;
import net.pkhapps.ddd.shared.infra.eventlog.StoredDomainEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class OrderProcessedEventTranslator implements RemoteEventTranslator {

    private final ObjectMapper objectMapper;

    OrderProcessedEventTranslator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(@NonNull StoredDomainEvent remoteEvent) {
        return remoteEvent.domainEventClassName().equals("net.pkhapps.ddd.orders.domain.model.event.OrderStateChanged");
    }

    @Override
    @NonNull
    public Optional<DomainEvent> translate(@NonNull StoredDomainEvent remoteEvent) {
        var orderStateChanged = remoteEvent.toDomainEvent(objectMapper, OrderStateChangedEvent.class);
        if (orderStateChanged.state().equals("PROCESSED")) {
            return Optional.of(new OrderProcessedEvent(orderStateChanged.orderId(), orderStateChanged.occurredOn()));
        }
        return Optional.empty();
    }
}
