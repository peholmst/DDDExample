package net.pkhapps.ddd.shared.domain.financial;

import org.springframework.lang.NonNull;

/**
 * Domain service interface for converting between currencies.
 */
public interface CurrencyConverter {

    /**
     * Converts the {@code amount} to the {@code newCurrency}.
     *
     * @param amount      the amount to convert.
     * @param newCurrency the currency to convert to.
     * @return the converted amount in the new currency.
     */
    @NonNull
    Money convert(@NonNull Money amount, @NonNull Currency newCurrency);
}
