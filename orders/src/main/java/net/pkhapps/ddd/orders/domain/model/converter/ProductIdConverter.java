package net.pkhapps.ddd.orders.domain.model.converter;

import net.pkhapps.ddd.orders.domain.model.ProductId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProductIdConverter implements AttributeConverter<ProductId, String> {
    @Override
    public String convertToDatabaseColumn(ProductId attribute) {
        return attribute == null ? null : attribute.toUUID();
    }

    @Override
    public ProductId convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new ProductId(dbData);
    }
}
