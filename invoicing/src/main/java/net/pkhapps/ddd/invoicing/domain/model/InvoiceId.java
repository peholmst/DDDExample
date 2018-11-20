package net.pkhapps.ddd.invoicing.domain.model;

import net.pkhapps.ddd.shared.domain.base.DomainObjectId;
import org.springframework.lang.NonNull;

public class InvoiceId extends DomainObjectId {
    public InvoiceId(@NonNull String uuid) {
        super(uuid);
    }
}
