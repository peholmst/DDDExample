package net.pkhapps.ddd.shared.domain.base;

/**
 * Interface for domain objects that can be softly deleted, meaning the domain object is not physically removed from
 * anywhere but only marked as deleted.
 */
public interface DeletableDomainObject extends DomainObject {

    /**
     * Returns whether this domain object has been marked as deleted.
     */
    boolean isDeleted();

    /**
     * Marks this domain object as deleted.
     */
    void delete();
}
