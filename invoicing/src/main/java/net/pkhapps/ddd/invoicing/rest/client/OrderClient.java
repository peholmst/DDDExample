package net.pkhapps.ddd.invoicing.rest.client;

import net.pkhapps.ddd.invoicing.domain.model.OrderId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class OrderClient {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    OrderClient(@Value("${app.orders.url}") String serverUrl,
                @Value("${app.orders.connect-timeout-ms}") int connectTimeout,
                @Value("${app.orders.read-timeout-ms}") int readTimeout) {
        this.serverUrl = serverUrl;
        restTemplate = new RestTemplate();
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        restTemplate.setRequestFactory(requestFactory);
    }

    @NonNull
    public Optional<Order> findById(@NonNull OrderId orderId) {
        var uri = UriComponentsBuilder.fromUriString(serverUrl).path("/api/orders/{id}");
        try {
            ResponseEntity<Order> response = restTemplate.getForEntity(uri.build(orderId.toUUID()), Order.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
