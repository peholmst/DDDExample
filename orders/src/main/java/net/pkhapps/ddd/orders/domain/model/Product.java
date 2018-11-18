package net.pkhapps.ddd.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.ValueObject;
import net.pkhapps.ddd.shared.domain.financial.Money;
import net.pkhapps.ddd.shared.domain.financial.VAT;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class Product implements ValueObject {

    private final ProductId id;
    private final String name;
    private final VAT valueAddedTax;
    private final Money price;

    @JsonCreator
    public Product(@JsonProperty("id") @NonNull ProductId id,
                   @JsonProperty("name") @NonNull String name,
                   @JsonProperty("valueAddedTax") @NonNull VAT valueAddedTax,
                   @JsonProperty("priceExcludingVAT") @NonNull Money price) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.valueAddedTax = Objects.requireNonNull(valueAddedTax, "valueAddedTax must not be null");
        this.price = Objects.requireNonNull(price, "price must not be null");
    }

    @NonNull
    public ProductId id() {
        return id;
    }

    @NonNull
    public String name() {
        return name;
    }

    @NonNull
    public VAT valueAddedTax() {
        return valueAddedTax;
    }

    @NonNull
    public Money price() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(valueAddedTax, product.valueAddedTax) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, valueAddedTax, price);
    }
}
