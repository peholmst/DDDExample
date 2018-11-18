@TypeDefs({
        @TypeDef(defaultForType = OrderId.class, typeClass = OrderIdType.class),
        @TypeDef(defaultForType = OrderItemId.class, typeClass = OrderItemIdType.class)
})
package net.pkhapps.ddd.orders.infra.hibernate;

import net.pkhapps.ddd.orders.domain.model.OrderId;
import net.pkhapps.ddd.orders.domain.model.OrderItemId;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;