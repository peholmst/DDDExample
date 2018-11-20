package net.pkhapps.ddd.shared.domain.base;

import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * Interface for domain objects that can be uniquely identified.
 *
 * @param <ID> the ID type.
 */
public interface IdentifiableDomainObject<ID extends Serializable> extends DomainObject {

    /**
     * Returns the ID of this domain object.
     *
     * @return the ID or {@code null} if an ID has not been assigned yet.
     */
    @Nullable
    ID id();
}
