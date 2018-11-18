package net.pkhapps.ddd.orders.application.form;

import net.pkhapps.ddd.shared.domain.financial.Currency;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderForm implements Serializable {

    @NotNull
    private Currency currency;
    @Valid
    @NotNull
    private RecipientAddressForm billingAddress = new RecipientAddressForm();
    @Valid
    @NotNull
    private RecipientAddressForm shippingAddress = new RecipientAddressForm();
    @Valid
    @NotEmpty
    private List<OrderItemForm> items = new ArrayList<>();

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public RecipientAddressForm getBillingAddress() {
        return billingAddress;
    }

    public RecipientAddressForm getShippingAddress() {
        return shippingAddress;
    }

    public List<OrderItemForm> getItems() {
        return items;
    }
}
