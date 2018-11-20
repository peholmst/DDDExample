package net.pkhapps.ddd.invoicing.domain.model;

import net.pkhapps.ddd.shared.domain.base.DomainObjectId;
import org.springframework.lang.NonNull;

public class OrderId extends DomainObjectId {
    public OrderId(@NonNull String uuid) {
        super(uuid);
    }
}
