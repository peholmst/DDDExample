package net.pkhapps.ddd.shipping.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import net.pkhapps.ddd.shared.infra.eventlog.RemoteEventTranslator;
import net.pkhapps.ddd.shared.infra.eventlog.StoredDomainEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
class OrderCreatedEventTranslator implements RemoteEventTranslator {

    private final ObjectMapper objectMapper;

    OrderCreatedEventTranslator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(@Nonnull StoredDomainEvent remoteEvent) {
        return remoteEvent.domainEventClassName().equals("net.pkhapps.ddd.orders.domain.model.event.OrderCreated");
    }

    @Override
    @Nonnull
    public DomainEvent translate(@Nonnull StoredDomainEvent remoteEvent) {
        return remoteEvent.toDomainEvent(objectMapper, OrderCreatedEvent.class);
    }
}
