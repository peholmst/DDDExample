package net.pkhapps.ddd.shared.infra.eventlog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

/**
 * Database entity for storing {@link DomainEvent}s as JSON in a relational database.
 */
@Entity
@Table(name = "event_log")
public class StoredDomainEvent {

    private static final int DOMAIN_EVENT_JSON_MAX_LENGTH = 1024;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    private Long id;
    @Column(name = "occurred_on", nullable = false)
    @JsonProperty("occurredOn")
    private Instant occurredOn;
    @Column(name = "domain_event_class_name", nullable = false)
    @JsonProperty("domainEventClass")
    private String domainEventClassName;
    @Column(name = "domain_event_body", nullable = false, length = DOMAIN_EVENT_JSON_MAX_LENGTH)
    @JsonProperty("domainEventBody")
    @JsonRawValue
    private String domainEventBody;
    @Transient
    private Class<? extends DomainEvent> domainEventClass;

    @SuppressWarnings("unused") // Used by JPA only
    private StoredDomainEvent() {
    }

    /**
     * Creates a new {@code StoredDomainEvent}.
     *
     * @param domainEvent  the domain event to store.
     * @param objectMapper the object mapper to use to convert the domain event into JSON.
     * @throws IllegalArgumentException if the domain event cannot be converted to JSON.
     */
    StoredDomainEvent(@NonNull DomainEvent domainEvent, @NonNull ObjectMapper objectMapper) {
        Objects.requireNonNull(domainEvent, "domainEvent must not be null");
        Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        occurredOn = domainEvent.occurredOn();
        domainEventClass = domainEvent.getClass();
        domainEventClassName = domainEventClass.getName();
        try {
            domainEventBody = objectMapper.writeValueAsString(domainEvent);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Could not serialize domain event to JSON", ex);
        }
        if (domainEventBody.length() > DOMAIN_EVENT_JSON_MAX_LENGTH) {
            throw new IllegalArgumentException("Domain event JSON string is too long");
        }
    }

    @NonNull
    public Long id() {
        if (id == null) {
            throw new IllegalStateException("The StoredDomainEvent has not been saved yet");
        }
        return id;
    }

    /**
     * Returns the domain event deserialized to {@link #domainEventClass()}.
     *
     * @param objectMapper the object mapper to use for parsing JSON.
     * @throws IllegalStateException if the JSON string cannot be turned into a domain event of the correct class.
     */
    @NonNull
    public DomainEvent toDomainEvent(@NonNull ObjectMapper objectMapper) {
        return toDomainEvent(objectMapper, domainEventClass());
    }

    /**
     * Returns the domain event.
     *
     * @param objectMapper     the object mapper to use for parsing JSON.
     * @param domainEventClass the class to deserialize to.
     * @throws IllegalStateException if the JSON string cannot be turned into a domain event of the correct class.
     */
    @NonNull
    public <T extends DomainEvent> T toDomainEvent(@NonNull ObjectMapper objectMapper,
                                                   @NonNull Class<T> domainEventClass) {
        Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        Objects.requireNonNull(domainEventClass, "domainEventClass must not be null");
        try {
            return objectMapper.readValue(domainEventBody, domainEventClass);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not deserialize domain event from JSON", ex);
        }
    }

    /**
     * Returns the domain event as a JSON string.
     */
    @NonNull
    public String toJsonString() {
        return domainEventBody;
    }

    /**
     * Returns the domain event as a {@link JsonNode}.
     *
     * @param objectMapper the object mapper to use for parsing JSON.
     * @throws IllegalStateException if the JSON string cannot be parsed into a JSON node.
     */
    @NonNull
    public JsonNode toJsonNode(@NonNull ObjectMapper objectMapper) {
        Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        try {
            return objectMapper.readTree(domainEventBody);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not deserialize domain event from JSON", ex);
        }
    }

    /**
     * Returns the class of the domain event.
     *
     * @throws IllegalStateException if the class does not exist in the class path.
     */
    @NonNull
    public Class<? extends DomainEvent> domainEventClass() {
        if (domainEventClass == null) {
            domainEventClass = lookupDomainEventClass();
        }
        return domainEventClass;
    }

    /**
     * Returns the name of the class of the domain event.
     */
    @NonNull
    public String domainEventClassName() {
        return domainEventClassName;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends DomainEvent> lookupDomainEventClass() {
        try {
            return (Class<? extends DomainEvent>) Class.forName(domainEventClassName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load domain event class", ex);
        }
    }

    /**
     * Returns the date and time when the domain event occurred.
     */
    @NonNull
    public Instant occurredOn() {
        return occurredOn;
    }
}
