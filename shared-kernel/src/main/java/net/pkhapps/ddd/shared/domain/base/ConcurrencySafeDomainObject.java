package net.pkhapps.ddd.shared.domain.base;

import org.springframework.lang.Nullable;

/**
 * Interface for domain objects that use optimistic locking to prevent multiple concurrent sessions from updating the
 * object at the same time.
 */
public interface ConcurrencySafeDomainObject extends DomainObject {

    /**
     * Returns the optimistic locking version of this domain object.
     *
     * @return the version or {@code null} if no version has been assigned yet.
     */
    @Nullable
    Long version();
}
