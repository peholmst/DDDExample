package net.pkhapps.ddd.shared.infra.eventlog;

import net.pkhapps.ddd.shared.domain.base.ValueObject;

import java.util.Objects;

/**
 * Value object representing the identifier of a {@link DomainEventLog}. In practice, the identifier consists of the
 * lowest and highest {@link StoredDomainEvent#id() ID} to include in the log.
 */
public class DomainEventLogId implements ValueObject {

    private final long low;
    private final long high;

    public DomainEventLogId(long low, long high) {
        if (low > high) {
            throw new IllegalArgumentException("low cannot be higher than high");
        }
        this.low = low;
        this.high = high;
    }

    /**
     * Returns the lowest {@link StoredDomainEvent#id()} ID to include in the log.
     */
    public long low() {
        return low;
    }

    /**
     * Returns the highest {@link StoredDomainEvent#id()} ID to include in the log.
     */
    public long high() {
        return high;
    }

    /**
     * Returns whether this domain event log ID refers to the first domain event log.
     */
    boolean isFirst() {
        return low == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (DomainEventLogId) o;
        return Objects.equals(low, that.low) &&
                Objects.equals(high, that.high);
    }

    @Override
    public int hashCode() {
        return Objects.hash(low, high);
    }

    @Override
    public String toString() {
        return String.format("%s[%d,%d]", getClass().getSimpleName(), low, high);
    }
}
