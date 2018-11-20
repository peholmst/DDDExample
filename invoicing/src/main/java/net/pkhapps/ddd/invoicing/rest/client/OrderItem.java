package net.pkhapps.ddd.invoicing.rest.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.financial.Money;
import net.pkhapps.ddd.shared.domain.financial.VAT;

public class OrderItem {

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private Money price;

    @JsonProperty("valueAddedTax")
    private VAT vat;

    @JsonProperty("qty")
    private int qty;

    OrderItem() {
    }

    public String description() {
        return description;
    }

    public Money price() {
        return price;
    }

    public VAT vat() {
        return vat;
    }

    public int qty() {
        return qty;
    }
}
