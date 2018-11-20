package net.pkhapps.ddd.shared.infra.eventlog;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Interface for a domain event log that exists on a remote machine.
 *
 * @see RemoteEventLogService
 */
public interface RemoteEventLog {

    /**
     * Returns whether this log is the current log (the one that new events are being appended to).
     */
    boolean isCurrent();

    /**
     * Returns the previous log if available.
     */
    @NonNull
    Optional<RemoteEventLog> previous();

    /**
     * Returns the next log if available.
     */
    @NonNull
    Optional<RemoteEventLog> next();

    /**
     * Checks if this event log contains a {@link StoredDomainEvent} with the given {@link StoredDomainEvent#id() ID}.
     */
    default boolean containsEvent(@NonNull Long eventId) {
        return events().stream().anyMatch(event -> eventId.equals(event.id()));
    }

    /**
     * Returns all events in the log.
     */
    @NonNull
    List<StoredDomainEvent> events();
}
