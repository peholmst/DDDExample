package net.pkhapps.ddd.shared.ui.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import net.pkhapps.ddd.shared.domain.geo.CityName;

public class StringToCityNameConverter implements Converter<String, CityName> {

    @Override
    public Result<CityName> convertToModel(String value, ValueContext context) {
        return value == null ? Result.ok(null) : Result.ok(new CityName(value));
    }

    @Override
    public String convertToPresentation(CityName value, ValueContext context) {
        return value == null ? "" : value.toString();
    }
}
