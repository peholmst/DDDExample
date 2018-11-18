package net.pkhapps.ddd.orders.application.form;

import net.pkhapps.ddd.shared.domain.geo.CityName;
import net.pkhapps.ddd.shared.domain.geo.Country;
import net.pkhapps.ddd.shared.domain.geo.PostalCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RecipientAddressForm implements Serializable {

    @NotEmpty
    private String name;
    @NotEmpty
    private String addressLine1;
    private String addressLine2;
    @NotNull
    private CityName city;
    @NotNull
    private PostalCode postalCode;
    @NotNull
    private Country country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public CityName getCity() {
        return city;
    }

    public void setCity(CityName city) {
        this.city = city;
    }

    public PostalCode getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(PostalCode postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
