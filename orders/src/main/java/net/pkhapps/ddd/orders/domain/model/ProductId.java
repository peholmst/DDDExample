package net.pkhapps.ddd.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.pkhapps.ddd.shared.domain.base.DomainObjectId;

public class ProductId extends DomainObjectId {
    @JsonCreator
    public ProductId(String uuid) {
        super(uuid);
    }
}
