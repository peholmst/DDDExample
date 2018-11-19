package net.pkhapps.ddd.shared.rest.client;

import net.pkhapps.ddd.shared.infra.eventlog.RemoteEventLog;
import net.pkhapps.ddd.shared.infra.eventlog.RemoteEventLogService;
import net.pkhapps.ddd.shared.infra.eventlog.StoredDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO Document me!
 */
public class RemoteEventLogServiceClient implements RemoteEventLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteEventLogService.class);

    private final String source;
    private final URI currentLogUri;
    private final RestTemplate restTemplate;

    public RemoteEventLogServiceClient(@NonNull String serverUrl, int connectTimeout, int readTimeout) {
        this.source = Objects.requireNonNull(serverUrl, "serverUrl must not be null");
        currentLogUri = UriComponentsBuilder.fromUriString(serverUrl).path("/api/event-log").build().toUri();
        restTemplate = new RestTemplate();
        var requestFactory = new SimpleClientHttpRequestFactory();
        // Never ever do a remote call without a finite timeout!
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        restTemplate.setRequestFactory(requestFactory);
        LOGGER.info("Reading remote domain events from {}", serverUrl);
    }

    @Override
    @NonNull
    public String source() {
        return source;
    }

    @Override
    @NonNull
    public RemoteEventLog currentLog() {
        return retrieveLog(currentLogUri);
    }

    @NonNull
    private RemoteEventLog retrieveLog(@NonNull URI uri) {
        LOGGER.debug("Retrieving remote log from {}", uri);
        ResponseEntity<List<StoredDomainEvent>> response = restTemplate.exchange(uri, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<StoredDomainEvent>>() {
                });
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Could not retrieve log from URI " + uri);
        }
        return new RemoteEventLogImpl(response);
    }

    private class RemoteEventLogImpl implements RemoteEventLog {

        private final List<StoredDomainEvent> events;
        private final URI previousLogUri;
        private final URI nextLogUri;
        private final URI thisUri;

        private RemoteEventLogImpl(@NonNull ResponseEntity<List<StoredDomainEvent>> responseEntity) {
            events = List.copyOf(Objects.requireNonNull(responseEntity.getBody()));
            previousLogUri = extractLink(responseEntity.getHeaders(), "previous");
            nextLogUri = extractLink(responseEntity.getHeaders(), "next");
            thisUri = extractLink(responseEntity.getHeaders(), "self");
        }

        @Override
        public boolean isCurrent() {
            return nextLogUri == null;
        }

        @Override
        public Optional<RemoteEventLog> previous() {
            if (previousLogUri != null) {
                return Optional.of(retrieveLog(previousLogUri));
            } else {
                return Optional.empty();
            }
        }

        @Override
        public Optional<RemoteEventLog> next() {
            if (nextLogUri != null) {
                return Optional.of(retrieveLog(nextLogUri));
            } else {
                return Optional.empty();
            }
        }

        @Override
        public List<StoredDomainEvent> events() {
            return events;
        }

        @Nullable
        private URI extractLink(@NonNull HttpHeaders headers, @NonNull String rel) {
            return headers.get("Link").stream()
                    .filter(link -> link.endsWith("rel=\"" + rel + "\""))
                    .findFirst()
                    .map(link -> link.substring(1, link.indexOf('>')))
                    .map(URI::create)
                    .orElse(null);
        }

        @Override
        public String toString() {
            return String.format("%s[self=%s, previous=%s, next=%s]", RemoteEventLog.class.getSimpleName(), thisUri, previousLogUri, nextLogUri);
        }
    }
}
