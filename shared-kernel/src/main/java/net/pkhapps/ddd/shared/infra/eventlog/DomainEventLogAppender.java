package net.pkhapps.ddd.shared.infra.eventlog;

import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * The domain event log appender listens for all {@link DomainEvent}s that are published and
 * {@link DomainEventLogService#append(DomainEvent) appends} them to the domain event log. The domain event is stored
 * inside the same transaction that published the event so if the transaction fails, no event is stored.
 */
@Service
class DomainEventLogAppender {

    private final DomainEventLogService domainEventLogService;

    DomainEventLogAppender(DomainEventLogService domainEventLogService) {
        this.domainEventLogService = domainEventLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onDomainEvent(@NonNull DomainEvent domainEvent) {
        domainEventLogService.append(domainEvent);
    }
}
