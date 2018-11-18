package net.pkhapps.ddd.shared.domain.financial;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Value object representing an amount of money. The amount is stored as a fixed-point integer where the last two digits
 * represent the decimals.
 */
public class Money implements ValueObject {

    @JsonProperty("currency")
    private final Currency currency;
    @JsonProperty("amount")
    private final int amount;

    /**
     * Creates a new {@code Money} object.
     *
     * @param currency the currency.
     * @param amount   fixed-point integer where the last two digits represent decimals.
     */
    @JsonCreator
    public Money(@NonNull @JsonProperty("currency") Currency currency, @JsonProperty("amount") int amount) {
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
        this.amount = amount;
    }

    /**
     * Creates a new {@code Money} object.
     *
     * @param currency the currency.
     * @param amount   the amount as a double.
     */
    public Money(@NonNull Currency currency, double amount) {
        this(currency, (int) (amount * 100));
    }

    /**
     * Creates a new {@code Money} object if both of the parameters are non-{@code null}.
     *
     * @param currency the currency.
     * @param value    fixed-point integer where the last two digits represent decimals.
     * @return a new {@code Money} object or {@code null} if any of the parameters are {@code null}.
     */
    public static Money valueOf(Currency currency, Integer value) {
        if (currency == null || value == null) {
            return null;
        } else {
            return new Money(currency, value);
        }
    }

    /**
     * Returns a new {@code Money} object whose amount is the sum of this amount and {@code augend}'s amount.
     *
     * @param augend the {@code Money} object to add to this object.
     * @return {@code this} + {@code augend}
     * @throws IllegalArgumentException if this object and {@code augend} have different currencies.
     */
    @NonNull
    public Money add(@NonNull Money augend) {
        Objects.requireNonNull(augend, "augend must not be null");
        if (currency != augend.currency) {
            throw new IllegalArgumentException("Cannot add two Money objects with different currencies");
        }
        return new Money(currency, amount + augend.amount);
    }

    /**
     * Returns a new {@code Money} object whose amount is the difference between this amount and {@code subtrahend}'s amount.
     *
     * @param subtrahend the {@code Money} object to remove from this object.
     * @return {@code this} - {@code augend}
     * @throws IllegalArgumentException if this object and {@code subtrahend} have different currencies.
     */
    @NonNull
    public Money subtract(@NonNull Money subtrahend) {
        Objects.requireNonNull(subtrahend, "subtrahend must not be null");
        if (currency != subtrahend.currency) {
            throw new IllegalArgumentException("Cannot subtract two Money objects with different currencies");
        }
        return new Money(currency, amount - subtrahend.amount);
    }

    /**
     * Returns a new {@code Money} object whose amount is this amount multiplied by {@code multiplicand}.
     *
     * @param multiplicand the value to multiply the amount by.
     * @return {@code this} * {@code multiplicand}
     */
    @NonNull
    public Money multiply(int multiplicand) {
        return new Money(currency, amount * multiplicand);
    }

    /**
     * Returns the currency.
     */
    @NonNull
    public Currency currency() {
        return currency;
    }

    /**
     * Returns the amount as a fixed-point integer where the last two digits represent decimals.
     */
    public int fixedPointAmount() {
        return amount;
    }

    /**
     * Returns the amount as a double.
     */
    public double doubleAmount() {
        return amount / 100d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount &&
                currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }

    @Override
    public String toString() {
        String amountString;
        if (amount == 0) {
            amountString = "000";
        } else {
            amountString = Integer.toString(amount);
        }
        return String.format("%s %s.%s", currency, amountString.substring(0, amountString.length() - 2),
                amountString.substring(amountString.length() - 2));
    }
}
