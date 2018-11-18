package net.pkhapps.ddd.shared.domain.financial.converter;

import net.pkhapps.ddd.shared.domain.financial.VAT;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA attribute converter for {@link VAT}.
 */
@Converter(autoApply = true)
public class VATAttributeConverter implements AttributeConverter<VAT, Integer> {

    @Override
    public Integer convertToDatabaseColumn(VAT attribute) {
        return attribute == null ? null : attribute.toInteger();
    }

    @Override
    public VAT convertToEntityAttribute(Integer dbData) {
        return VAT.valueOf(dbData);
    }
}
