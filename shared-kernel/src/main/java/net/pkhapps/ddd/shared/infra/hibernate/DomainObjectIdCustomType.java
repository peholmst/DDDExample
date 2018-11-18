package net.pkhapps.ddd.shared.infra.hibernate;

import net.pkhapps.ddd.shared.domain.base.DomainObjectId;
import org.hibernate.id.ResultSetIdentifierConsumer;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Hibernate custom type for a {@link DomainObjectId} subtype. You need this to be able to use {@link DomainObjectId}s
 * as primary keys. You have to create one subclass per {@link DomainObjectId} subtype.
 *
 * @param <ID> the ID type.
 * @see DomainObjectIdTypeDescriptor
 */
public abstract class DomainObjectIdCustomType<ID extends DomainObjectId> extends AbstractSingleColumnStandardBasicType<ID>
        implements ResultSetIdentifierConsumer {

    /**
     * Creates a new {@code DomainObjectIdCustomType}. In your subclass, you should create a default constructor and
     * invoke this constructor from there.
     *
     * @param domainObjectIdTypeDescriptor the {@link DomainObjectIdTypeDescriptor} for the ID type.
     */
    public DomainObjectIdCustomType(@NonNull DomainObjectIdTypeDescriptor<ID> domainObjectIdTypeDescriptor) {
        super(VarcharTypeDescriptor.INSTANCE, domainObjectIdTypeDescriptor);
    }

    @Override
    public Serializable consumeIdentifier(ResultSet resultSet) {
        try {
            var id = resultSet.getString(1);
            return getJavaTypeDescriptor().fromString(id);
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not extract ID from ResultSet", ex);
        }
    }

    @Override
    public String getName() {
        return getJavaTypeDescriptor().getJavaType().getSimpleName();
    }
}
