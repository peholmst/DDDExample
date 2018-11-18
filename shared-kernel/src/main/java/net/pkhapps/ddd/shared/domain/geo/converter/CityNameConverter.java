package net.pkhapps.ddd.shared.domain.geo.converter;

import net.pkhapps.ddd.shared.domain.geo.CityName;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA attribute converter for {@link CityName}.
 */
@Converter(autoApply = true)
public class CityNameConverter implements AttributeConverter<CityName, String> {

    @Override
    public String convertToDatabaseColumn(CityName attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public CityName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new CityName(dbData);
    }
}
