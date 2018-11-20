package net.pkhapps.ddd.shipping.domain.converter;

import net.pkhapps.ddd.shipping.domain.OrderId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class OrderIdConverter implements AttributeConverter<OrderId, String> {

    @Override
    public String convertToDatabaseColumn(OrderId attribute) {
        return attribute == null ? null : attribute.toUUID();
    }

    @Override
    public OrderId convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new OrderId(dbData);
    }
}
