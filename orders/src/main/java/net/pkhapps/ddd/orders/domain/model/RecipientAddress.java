package net.pkhapps.ddd.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.geo.Address;
import net.pkhapps.ddd.shared.domain.geo.CityName;
import net.pkhapps.ddd.shared.domain.geo.Country;
import net.pkhapps.ddd.shared.domain.geo.PostalCode;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class RecipientAddress extends Address {

    @Column(name = "recipient_name")
    private String name;

    @SuppressWarnings("unused") // Used by JPA only.
    protected RecipientAddress() {
    }

    public RecipientAddress(@NonNull String name, @NonNull String addressLine1, @Nullable String addressLine2,
                            @NonNull CityName city, @NonNull PostalCode postalCode, @NonNull Country country) {
        super(addressLine1, addressLine2, city, postalCode, country);
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    @NonNull
    @JsonProperty("name")
    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RecipientAddress that = (RecipientAddress) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
