package net.pkhapps.ddd.orders.domain.model;

import net.pkhapps.ddd.shared.domain.base.DomainObjectId;

public class OrderItemId extends DomainObjectId {
    public OrderItemId(String uuid) {
        super(uuid);
    }
}
