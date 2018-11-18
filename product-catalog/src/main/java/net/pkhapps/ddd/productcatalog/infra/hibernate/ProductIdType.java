package net.pkhapps.ddd.productcatalog.infra.hibernate;

import net.pkhapps.ddd.productcatalog.domain.model.ProductId;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdCustomType;
import net.pkhapps.ddd.shared.infra.hibernate.DomainObjectIdTypeDescriptor;

/**
 * Hibernate custom type for {@link ProductId}.
 */
public class ProductIdType extends DomainObjectIdCustomType<ProductId> {

    private static final DomainObjectIdTypeDescriptor<ProductId> TYPE_DESCRIPTOR = new DomainObjectIdTypeDescriptor<>(
            ProductId.class, ProductId::new);

    public ProductIdType() {
        super(TYPE_DESCRIPTOR);
    }
}
