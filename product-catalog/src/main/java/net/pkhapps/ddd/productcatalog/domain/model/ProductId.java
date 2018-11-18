package net.pkhapps.ddd.productcatalog.domain.model;

import net.pkhapps.ddd.shared.domain.base.DomainObjectId;

/**
 * Value object representing a {@link Product} ID.
 */
public class ProductId extends DomainObjectId {
    public ProductId(String uuid) {
        super(uuid);
    }
}
