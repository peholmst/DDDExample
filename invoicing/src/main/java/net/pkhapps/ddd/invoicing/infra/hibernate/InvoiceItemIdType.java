package net.pkhapps.ddd.invoicing.infra.hibernate;

import net.pkhapps.ddd.invoicing.domain.model.InvoiceItemId;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdCustomType;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdTypeDescriptor;

public class InvoiceItemIdType extends DomainObjectIdCustomType<InvoiceItemId> {

    private static final DomainObjectIdTypeDescriptor<InvoiceItemId> TYPE_DESCRIPTOR =
            new DomainObjectIdTypeDescriptor<>(InvoiceItemId.class, InvoiceItemId::new);

    public InvoiceItemIdType() {
        super(TYPE_DESCRIPTOR);
    }
}
