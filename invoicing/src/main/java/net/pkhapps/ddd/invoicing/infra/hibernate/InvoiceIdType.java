package net.pkhapps.ddd.invoicing.infra.hibernate;

import net.pkhapps.ddd.invoicing.domain.model.InvoiceId;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdCustomType;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdTypeDescriptor;

public class InvoiceIdType extends DomainObjectIdCustomType<InvoiceId> {

    private static final DomainObjectIdTypeDescriptor<InvoiceId> TYPE_DESCRIPTOR =
            new DomainObjectIdTypeDescriptor<>(InvoiceId.class, InvoiceId::new);

    public InvoiceIdType() {
        super(TYPE_DESCRIPTOR);
    }
}
