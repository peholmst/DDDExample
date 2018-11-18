package net.pkhapps.ddd.shared.rest.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.ddd.shared.domain.financial.Currency;
import net.pkhapps.ddd.shared.domain.financial.CurrencyConverter;
import net.pkhapps.ddd.shared.domain.financial.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link CurrencyConverter} that uses https://exchangeratesapi.io/ to fetch the latest rates.
 */
@Service
class CurrencyConverterClient implements CurrencyConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConverterClient.class);
    private final RestTemplate restTemplate;
    private final Map<Currency, Double> conversionRates = new ConcurrentHashMap<>();
    private final boolean enabled;

    public CurrencyConverterClient(@Value("${app.currency-conversion.enabled:false}") boolean enabled) {
        this.enabled = enabled;
        restTemplate = new RestTemplate();
        var requestFactory = new SimpleClientHttpRequestFactory();
        // Never ever do a remote call without a finite timeout!
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        restTemplate.setRequestFactory(requestFactory);
    }

    @Override
    @NonNull
    public Money convert(@NonNull Money amount, @NonNull Currency newCurrency) {
        if (amount.currency() == newCurrency) {
            return amount;
        } else {
            var euro = convertToEuro(amount);
            return convertEuroTo(euro, newCurrency);
        }
    }

    private Money convertToEuro(@NonNull Money amount) {
        if (amount.currency() == Currency.EUR) {
            return amount;
        } else {
            return new Money(Currency.EUR, amount.doubleAmount() / getConversionRateFor(amount.currency()));
        }
    }

    private Money convertEuroTo(@NonNull Money amount, @NonNull Currency newCurrency) {
        return new Money(newCurrency, amount.doubleAmount() * getConversionRateFor(newCurrency));
    }

    private double getConversionRateFor(@NonNull Currency currency) {
        var rate = conversionRates.get(currency);
        if (rate == null) {
            throw new IllegalStateException("Missing conversion rate for " + currency);
        }
        return rate;
    }

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // Refresh every 6 hours
    public void fetchConversionRates() {
        if (!enabled) {
            LOGGER.info("Currency conversion is disabled. No rates are fetched.");
            return;
        }
        LOGGER.info("Fetching conversion rates from web service");
        var uri = UriComponentsBuilder.fromUriString("https://api.exchangeratesapi.io/latest")
                .queryParam("symbols", Stream.of(Currency.values())
                        .filter(c -> c != Currency.EUR)
                        .map(Enum::name).collect(Collectors.joining(","))).build().toUri();
        LOGGER.debug("Using URI {}", uri);
        var rates = restTemplate.getForEntity(uri, RatesDTO.class).getBody();
        if (rates != null) {
            conversionRates.putAll(rates.rates);
            LOGGER.info("Received {} rates", rates.rates.size());
        }
    }

    static class RatesDTO {

        @JsonProperty("date")
        LocalDate date;

        @JsonProperty("base")
        Currency base;

        @JsonProperty("rates")
        Map<Currency, Double> rates;
    }
}
