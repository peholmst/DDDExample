package net.pkhapps.ddd.shared.domain.geo;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.ValueObject;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@MappedSuperclass
public class Address implements ValueObject {

    @Column(name = "address_line1")
    private String addressLine1;
    @Column(name = "address_line2")
    private String addressLine2;
    @Column(name = "city")
    private CityName city;
    @Column(name = "postal_code")
    private PostalCode postalCode;
    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private Country country;

    @SuppressWarnings("unused") // Used by JPA only.
    protected Address() {
    }

    public Address(@NonNull String addressLine1, @Nullable String addressLine2, @NonNull CityName city,
                   @NonNull PostalCode postalCode, @NonNull Country country) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    @NonNull
    @JsonProperty("address1")
    public String addressLine1() {
        return addressLine1;
    }

    @Nullable
    @JsonProperty("address2")
    public String addressLine2() {
        return addressLine2;
    }

    @NonNull
    @JsonProperty("city")
    public CityName city() {
        return city;
    }

    @NonNull
    @JsonProperty("postalCode")
    public PostalCode postalCode() {
        return postalCode;
    }

    @NonNull
    @JsonProperty("country")
    public Country country() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(addressLine1, address.addressLine1) &&
                Objects.equals(addressLine2, address.addressLine2) &&
                Objects.equals(city, address.city) &&
                Objects.equals(postalCode, address.postalCode) &&
                country == address.country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressLine1, addressLine2, city, postalCode, country);
    }
}
