@TypeDefs({
        @TypeDef(defaultForType = InvoiceId.class, typeClass = InvoiceIdType.class),
        @TypeDef(defaultForType = InvoiceItemId.class, typeClass = InvoiceItemIdType.class)
})
package net.pkhapps.ddd.invoicing.infra.hibernate;

import net.pkhapps.ddd.invoicing.domain.model.InvoiceId;
import net.pkhapps.ddd.invoicing.domain.model.InvoiceItemId;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;