package net.pkhapps.ddd.invoicing.domain.model;

import net.pkhapps.ddd.shared.domain.base.DomainObjectId;
import org.springframework.lang.NonNull;

public class InvoiceItemId extends DomainObjectId {
    public InvoiceItemId(@NonNull String uuid) {
        super(uuid);
    }
}
