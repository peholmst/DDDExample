package net.pkhapps.ddd.shared.infra.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The domain event log is responsible for storing and retrieving {@link DomainEvent}s. These can be used for auditing
 * or for integration with other systems / bounded contexts.
 *
 * @see StoredDomainEvent
 * @see DomainEventLogAppender
 */
@Service
public class DomainEventLog {

    private static final int LOG_SIZE = 20;

    private final StoredDomainEventRepository storedDomainEventRepository;
    private final ObjectMapper objectMapper;

    DomainEventLog(StoredDomainEventRepository storedDomainEventRepository,
                   ObjectMapper objectMapper) {
        this.storedDomainEventRepository = storedDomainEventRepository;
        this.objectMapper = objectMapper;
    }

    private static long calculateHighFromLow(long low) {
        return low + LOG_SIZE - 1;
    }

    /**
     * Returns the domain events that are stored in the given log.
     *
     * @param logId the ID of the log to retrieve.
     * @return a stream of events stored in the given log, which can contain everything between 0 and {@value #LOG_SIZE} items.
     */
    @NonNull
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Stream<StoredDomainEvent> retrieve(@NonNull DomainEventLogId logId) {
        return storedDomainEventRepository.findEventsBetween(logId.low(), logId.high());
    }

    /**
     * Returns the ID of the current log. The current log can never be completely full since it becomes an archived
     * log immediately when the {@value #LOG_SIZE}:th event is {@link #append(DomainEvent) appended}.
     *
     * @return the current log ID.
     * @see #retrieve(DomainEventLogId)
     * @see #nextLogId(DomainEventLogId)
     * @see #previousLogId(DomainEventLogId)
     */
    @NonNull
    public DomainEventLogId currentLogId() {
        var max = storedDomainEventRepository.findHighestDomainEventId();
        var remainder = max % LOG_SIZE;
        if (remainder == 0 && max == 0) {
            remainder = LOG_SIZE;
        }

        var low = max - remainder + 1;
        if (low < 1) {
            low = 1;
        }
        var high = calculateHighFromLow(low);

        return new DomainEventLogId(low, high);
    }

    /**
     * Returns the ID of the log that comes just before the given log in the virtual array of logs.
     *
     * @param logId the log ID.
     * @return the previous log ID or an empty {@code Optional} if the given {@code logId} is the first log.
     */
    @NonNull
    public Optional<DomainEventLogId> previousLogId(@NonNull DomainEventLogId logId) {
        Objects.requireNonNull(logId, "logId must not be null");

        if (logId.isFirst()) {
            return Optional.empty();
        }

        var low = logId.low() - LOG_SIZE;
        if (low < 1) {
            low = 1;
        }
        var high = calculateHighFromLow(low);

        return Optional.of(new DomainEventLogId(low, high));
    }

    /**
     * Returns the ID of the log that comes just after the given log in the virtual array of logs.
     *
     * @param logId the log ID.
     * @return the next log ID.
     */
    @NonNull
    public DomainEventLogId nextLogId(@NonNull DomainEventLogId logId) {
        Objects.requireNonNull(logId, "logId must not be null");

        var low = logId.high() + 1;
        var high = calculateHighFromLow(low);

        return new DomainEventLogId(low, high);
    }

    /**
     * Appends the given domain event to the event log.
     *
     * @param domainEvent the domain event to append.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void append(@NonNull DomainEvent domainEvent) {
        var storedEvent = new StoredDomainEvent(domainEvent, objectMapper);
        storedDomainEventRepository.saveAndFlush(storedEvent);
    }
}
