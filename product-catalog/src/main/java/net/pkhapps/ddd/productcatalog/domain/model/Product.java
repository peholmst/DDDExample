package net.pkhapps.ddd.productcatalog.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.AbstractAggregateRoot;
import net.pkhapps.ddd.shared.domain.base.ConcurrencySafeDomainObject;
import net.pkhapps.ddd.shared.domain.base.DeletableDomainObject;
import net.pkhapps.ddd.shared.domain.base.DomainObjectId;
import net.pkhapps.ddd.shared.domain.financial.Currency;
import net.pkhapps.ddd.shared.domain.financial.Money;
import net.pkhapps.ddd.shared.domain.financial.VAT;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Objects;

/**
 * Aggregate root representing a product in the product catalog.
 */
@Entity
@Table(name = "products")
public class Product extends AbstractAggregateRoot<ProductId> implements DeletableDomainObject,
        ConcurrencySafeDomainObject {

    @Version
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency priceCurrency;

    @Column(name = "price_value", nullable = false)
    private Integer priceValue;

    @Column(name = "vat", nullable = false)
    private VAT vat;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @SuppressWarnings("unused") // Used by JPA only
    private Product() {
    }

    public Product(@NonNull String name, @NonNull Money price, @NonNull VAT vat) {
        super(DomainObjectId.randomId(ProductId.class));
        setName(name);
        setPrice(price);
        setVAT(vat);
    }

    @NonNull
    @JsonProperty("name")
    public String name() {
        return name;
    }

    private void setName(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    @Nullable
    @JsonProperty("description")
    public String description() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @NonNull
    @JsonProperty("priceExcludingVAT")
    public Money price() {
        return Money.valueOf(priceCurrency, priceValue);
    }

    @NonNull
    @JsonProperty("priceIncludingVAT")
    public Money priceIncludingVAT() {
        return vat().addTax(price());
    }

    private void setPrice(@NonNull Money price) {
        Objects.requireNonNull(price, "price must not be null");
        priceCurrency = price.currency();
        priceValue = price.fixedPointAmount();
    }

    @NonNull
    @JsonProperty("valueAddedTax")
    public VAT vat() {
        return vat;
    }

    private void setVAT(@NonNull VAT vat) {
        this.vat = Objects.requireNonNull(vat);
    }

    @Override
    @Nullable
    public Long version() {
        return version;
    }

    @Override
    @JsonProperty("isDeleted")
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void delete() {
        this.deleted = true;
    }
}
