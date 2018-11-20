package net.pkhapps.ddd.shipping.infra.hibernate;

import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdCustomType;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdTypeDescriptor;
import net.pkhapps.ddd.shipping.domain.PickingListId;

public class PickingListIdType extends DomainObjectIdCustomType<PickingListId> {
    private static final DomainObjectIdTypeDescriptor<PickingListId> TYPE_DESCRIPTOR = new DomainObjectIdTypeDescriptor<>(
            PickingListId.class, PickingListId::new);

    public PickingListIdType() {
        super(TYPE_DESCRIPTOR);
    }
}
