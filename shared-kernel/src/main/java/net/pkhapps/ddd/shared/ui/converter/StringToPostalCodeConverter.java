package net.pkhapps.ddd.shared.ui.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import net.pkhapps.ddd.shared.domain.geo.PostalCode;

public class StringToPostalCodeConverter implements Converter<String, PostalCode> {

    @Override
    public Result<PostalCode> convertToModel(String s, ValueContext valueContext) {
        return s == null ? Result.ok(null) : Result.ok(new PostalCode(s));
    }

    @Override
    public String convertToPresentation(PostalCode postalCode, ValueContext valueContext) {
        return postalCode == null ? "" : postalCode.toString();
    }
}
