package net.pkhapps.ddd.shipping.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.pkhapps.ddd.shared.domain.base.DomainObjectId;

public class OrderId extends DomainObjectId {
    @JsonCreator
    public OrderId(String uuid) {
        super(uuid);
    }
}
