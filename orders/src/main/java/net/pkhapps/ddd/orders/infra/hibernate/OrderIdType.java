package net.pkhapps.ddd.orders.infra.hibernate;

import net.pkhapps.ddd.orders.domain.model.OrderId;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdCustomType;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdTypeDescriptor;

public class OrderIdType extends DomainObjectIdCustomType<OrderId> {
    private static final DomainObjectIdTypeDescriptor<OrderId> TYPE_DESCRIPTOR = new DomainObjectIdTypeDescriptor<>(
            OrderId.class, OrderId::new);

    public OrderIdType() {
        super(TYPE_DESCRIPTOR);
    }
}
