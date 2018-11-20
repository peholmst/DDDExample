package net.pkhapps.ddd.shipping.rest.client;

import net.pkhapps.ddd.shipping.domain.OrderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
public class OrderCatalogClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCatalogClient.class);

    private final RestTemplate restTemplate;
    private final String serverUrl;

    OrderCatalogClient(@Value("${app.orders.url}") String serverUrl,
                       @Value("${app.orders.connect-timeout-ms}") int connectTimeout,
                       @Value("${app.orders.read-timeout-ms}") int readTimeout) {
        this.serverUrl = serverUrl;
        restTemplate = new RestTemplate();
        var requestFactory = new SimpleClientHttpRequestFactory();
        // Never ever do a remote call without a finite timeout!
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        restTemplate.setRequestFactory(requestFactory);
    }

    private UriComponentsBuilder uri() {
        return UriComponentsBuilder.fromUriString(serverUrl);
    }

    @Nonnull
    public Optional<Order> findById(@Nonnull OrderId orderId) {
        try {
            ResponseEntity<Order> response = restTemplate.getForEntity(uri().path("/api/orders/{id}").build(orderId.toUUID()), Order.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception ex) {
            LOGGER.error("Error retrieving order " + orderId, ex);
            return Optional.empty();
        }
    }

    public void startProcessing(@Nonnull OrderId orderId) {
        restTemplate.put(uri().path("/api/orders/{id}/startProcessing").build(orderId.toUUID()), null);
    }

    public void finishProcessing(@Nonnull OrderId orderId) {
        restTemplate.put(uri().path("/api/orders/{id}/finishProcessing").build(orderId.toUUID()), null);
    }
}
