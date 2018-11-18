package net.pkhapps.ddd.shared.domain.geo.converter;

import net.pkhapps.ddd.shared.domain.geo.PostalCode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA attribute converter for {@link PostalCode}.
 */
@Converter(autoApply = true)
public class PostalCodeConverter implements AttributeConverter<PostalCode, String> {

    @Override
    public String convertToDatabaseColumn(PostalCode attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public PostalCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new PostalCode(dbData);
    }
}
