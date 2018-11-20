package net.pkhapps.ddd.shared.infra.eventlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

/**
 * Internal service that regularly retrieve and process {@link StoredDomainEvent}s from all
 * {@link RemoteEventLogService}s that exist in the application context. {@link RemoteEventTranslator}s are used
 * to translate the events into {@link net.pkhapps.ddd.shared.domain.base.DomainEvent}s, which are then published on
 * the local {@link ApplicationEventPublisher application event bus}.
 */
@Service
class RemoteEventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteEventProcessor.class);

    private final ProcessedRemoteEventRepository processedRemoteEventRepository;
    private final Map<String, RemoteEventLogService> remoteEventLogs;
    private final Map<String, RemoteEventTranslator> remoteEventTranslators;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TransactionTemplate transactionTemplate;

    RemoteEventProcessor(@NonNull ProcessedRemoteEventRepository processedRemoteEventRepository,
                         @NonNull Map<String, RemoteEventLogService> remoteEventLogs,
                         @NonNull Map<String, RemoteEventTranslator> remoteEventTranslators,
                         @NonNull ApplicationEventPublisher applicationEventPublisher,
                         @NonNull PlatformTransactionManager platformTransactionManager) {
        this.processedRemoteEventRepository = processedRemoteEventRepository;
        this.remoteEventLogs = remoteEventLogs;
        this.remoteEventTranslators = remoteEventTranslators;
        this.applicationEventPublisher = applicationEventPublisher;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager,
                new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
    }

    /**
     * Retrieves and processes remote events.
     */
    @Scheduled(fixedDelay = 20000)
    public void processEvents() {
        remoteEventLogs.values().forEach(this::processEvents);
    }

    private void processEvents(@NonNull RemoteEventLogService remoteEventLogService) {
        LOGGER.info("Processing remote events from {}", remoteEventLogService.source());
        var lastProcessedId = getLastProcessedId(remoteEventLogService);

        var log = remoteEventLogService.currentLog();
        LOGGER.debug("Starting with current log {}", log);
        // Find the first log
        while (!log.containsEvent(lastProcessedId)) {
            var previous = log.previous();
            if (previous.isPresent()) {
                log = previous.get();
                LOGGER.debug("Checking previous log {}", log);
            } else {
                break;
            }
        }

        // Then, start processing events
        do {
            LOGGER.debug("Processing events in log {}", log);
            processEvents(remoteEventLogService, lastProcessedId, log.events());
            var next = log.next();
            if (next.isPresent()) {
                log = next.get();
            }
        } while (!log.isCurrent());

        LOGGER.info("Finished processing remote events from {}", remoteEventLogService.source());
    }

    private void processEvents(@NonNull RemoteEventLogService remoteEventLogService, long lastProcessedId,
                               @NonNull List<StoredDomainEvent> events) {
        events.forEach(event -> {
            if (event.id() > lastProcessedId) {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        LOGGER.debug("Processing remote event {} from {}", event, remoteEventLogService.source());
                        publishEvent(event);
                        setLastProcessedId(remoteEventLogService, event.id());
                    }
                });
            }
        });
    }

    private long getLastProcessedId(@NonNull RemoteEventLogService remoteEventLogService) {
        return processedRemoteEventRepository.findById(remoteEventLogService.source())
                .map(ProcessedRemoteEvent::lastProcessedEventId)
                .orElse(0L);
    }

    private void setLastProcessedId(@NonNull RemoteEventLogService remoteEventLogService, long lastProcessedId) {
        processedRemoteEventRepository.saveAndFlush(new ProcessedRemoteEvent(remoteEventLogService.source(), lastProcessedId));
    }

    private void publishEvent(@NonNull StoredDomainEvent event) {
        remoteEventTranslators.values().stream()
                .filter(translator -> translator.supports(event))
                .findFirst()
                .flatMap(translator -> translator.translate(event))
                .ifPresent(applicationEventPublisher::publishEvent);
    }
}
