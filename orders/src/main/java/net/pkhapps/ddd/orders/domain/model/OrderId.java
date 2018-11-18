package net.pkhapps.ddd.orders.domain.model;

import net.pkhapps.ddd.shared.domain.base.DomainObjectId;

public class OrderId extends DomainObjectId {
    public OrderId(String uuid) {
        super(uuid);
    }
}
