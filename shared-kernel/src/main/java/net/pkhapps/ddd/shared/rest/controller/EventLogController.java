package net.pkhapps.ddd.shared.rest.controller;

import net.pkhapps.ddd.shared.infra.eventlog.DomainEventLog;
import net.pkhapps.ddd.shared.infra.eventlog.DomainEventLogId;
import net.pkhapps.ddd.shared.infra.eventlog.DomainEventLogService;
import net.pkhapps.ddd.shared.infra.eventlog.StoredDomainEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller that exposes the domain event log as a REST service.
 *
 * @see net.pkhapps.ddd.shared.rest.client.RemoteEventLogServiceClient
 */
@RestController
@RequestMapping(path = EventLogController.ROOT)
class EventLogController {

    static final String ROOT = "/api/event-log";

    private final DomainEventLogService domainEventLogService;

    EventLogController(DomainEventLogService domainEventLogService) {
        this.domainEventLogService = domainEventLogService;
    }

    @GetMapping(path = "/{low},{high}")
    public ResponseEntity<List<StoredDomainEvent>> domainEvents(@PathVariable("low") long low,
                                                                @PathVariable("high") long high,
                                                                UriComponentsBuilder uriBuilder) {
        var logId = new DomainEventLogId(low, high);
        return domainEventLogService.retrieveLog(logId)
                .map(log -> createResponse(log, uriBuilder))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<StoredDomainEvent>> currentLog(UriComponentsBuilder uriBuilder) {
        return createResponse(domainEventLogService.currentLog(), uriBuilder);
    }

    @NonNull
    private ResponseEntity<List<StoredDomainEvent>> createResponse(@NonNull DomainEventLog log,
                                                                   @NonNull UriComponentsBuilder uriBuilder) {
        var responseBuilder = ResponseEntity.ok();
        log.previousId().ifPresent(previous -> addLink(responseBuilder, buildURI(uriBuilder, previous), "previous"));
        log.nextId().ifPresent(next -> addLink(responseBuilder, buildURI(uriBuilder, next), "next"));
        addLink(responseBuilder, buildURI(uriBuilder, log.id()), "self");
        return responseBuilder.body(log.events());
    }

    private void addLink(@NonNull ResponseEntity.BodyBuilder builder, @NonNull URI uri, @NonNull String rel) {
        builder.header("Link", String.format("<%s>; rel=\"%s\"", uri, rel));
    }

    @NonNull
    private URI buildURI(UriComponentsBuilder uriBuilder, @NonNull DomainEventLogId logId) {
        return uriBuilder.cloneBuilder().path(ROOT).path("/{low},{high}").build(logId.low(), logId.high());
    }
}
