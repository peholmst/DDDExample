package net.pkhapps.ddd.shared.infra.eventlog;

import java.util.Objects;

/**
 * TODO Document me
 */
public class DomainEventLogId {

    private final long low;
    private final long high;

    public DomainEventLogId(long low, long high) {
        this.low = low;
        this.high = high;
    }

    public long low() {
        return low;
    }

    public long high() {
        return high;
    }

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
