package net.pkhapps.ddd.shipping.rest.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.geo.CityName;
import net.pkhapps.ddd.shared.domain.geo.Country;
import net.pkhapps.ddd.shared.domain.geo.PostalCode;

public class RecipientAddress {

    @JsonProperty("name")
    private String name;
    @JsonProperty("address1")
    private String address1;
    @JsonProperty("address2")
    private String address2;
    @JsonProperty("postalCode")
    private PostalCode postalCode;
    @JsonProperty("city")
    private CityName cityName;
    @JsonProperty("country")
    private Country country;

    RecipientAddress() {
    }

    public String name() {
        return name;
    }

    public String address1() {
        return address1;
    }

    public String address2() {
        return address2;
    }

    public PostalCode postalCode() {
        return postalCode;
    }

    public CityName cityName() {
        return cityName;
    }

    public Country country() {
        return country;
    }
}
