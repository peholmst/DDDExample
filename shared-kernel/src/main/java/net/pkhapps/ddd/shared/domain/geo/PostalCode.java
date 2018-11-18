package net.pkhapps.ddd.shared.domain.geo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import net.pkhapps.ddd.shared.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Value object representing a postal code.
 */
public class PostalCode implements ValueObject {

    private final String postalCode;

    @JsonCreator
    public PostalCode(@NonNull String postalCode) {
        this.postalCode = Objects.requireNonNull(postalCode, "postalCode must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostalCode that = (PostalCode) o;
        return Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postalCode);
    }

    @Override
    @JsonValue
    public String toString() {
        return postalCode;
    }
}
