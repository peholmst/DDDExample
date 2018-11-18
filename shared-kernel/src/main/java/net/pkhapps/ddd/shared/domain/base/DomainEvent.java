package net.pkhapps.ddd.shared.domain.base;

import org.springframework.lang.NonNull;

import java.time.Instant;

/**
 * Interface for domain events.
 */
public interface DomainEvent extends DomainObject {

    /**
     * Returns the time and date on which the event occurred.
     */
    @NonNull
    Instant occurredOn();
}
