package net.pkhapps.ddd.orders.infra.hibernate;

import net.pkhapps.ddd.orders.domain.model.OrderItemId;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdCustomType;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdTypeDescriptor;

public class OrderItemIdType extends DomainObjectIdCustomType<OrderItemId> {
    private static final DomainObjectIdTypeDescriptor<OrderItemId> TYPE_DESCRIPTOR = new DomainObjectIdTypeDescriptor<>(
            OrderItemId.class, OrderItemId::new);

    public OrderItemIdType() {
        super(TYPE_DESCRIPTOR);
    }
}
