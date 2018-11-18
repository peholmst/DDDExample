package net.pkhapps.ddd.shared.infra.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link DomainEventLog}.
 */
public class DomainEventLogTest {

    private StoredDomainEventRepository storedDomainEventRepository;
    private ObjectMapper objectMapper;
    private DomainEventLog domainEventLog;

    @Before
    public void setUp() {
        storedDomainEventRepository = mock(StoredDomainEventRepository.class);
        objectMapper = mock(ObjectMapper.class);
        domainEventLog = new DomainEventLog(storedDomainEventRepository, objectMapper);
    }

    @Test
    public void currentLogId_noEvents_currentLogIsEmpty() {
        when(storedDomainEventRepository.findHighestDomainEventId()).thenReturn(0L);
        var currentLogId = domainEventLog.currentLogId();
        assertThat(currentLogId).isEqualTo(new DomainEventLogId(1, 20));
    }

    @Test
    public void currentLogId_oneEvent_currentLogIsNonEmpty() {
        when(storedDomainEventRepository.findHighestDomainEventId()).thenReturn(1L);
        var currentLogId = domainEventLog.currentLogId();
        assertThat(currentLogId).isEqualTo(new DomainEventLogId(1, 20));
    }

    @Test
    public void currentLogId_almostFullLog_currentLogIsNonEmpty() {
        when(storedDomainEventRepository.findHighestDomainEventId()).thenReturn(19L);
        var currentLogId = domainEventLog.currentLogId();
        assertThat(currentLogId).isEqualTo(new DomainEventLogId(1, 20));
    }

    @Test
    public void currentLogId_fullLog_currentLogIsEmpty() {
        when(storedDomainEventRepository.findHighestDomainEventId()).thenReturn(20L);
        var currentLogId = domainEventLog.currentLogId();
        assertThat(currentLogId).isEqualTo(new DomainEventLogId(21, 40));
    }

    @Test
    public void currentLogId_almostFullLogWithOneArchived_currentLogIsNonEmpty() {
        when(storedDomainEventRepository.findHighestDomainEventId()).thenReturn(39L);
        var currentLogId = domainEventLog.currentLogId();
        assertThat(currentLogId).isEqualTo(new DomainEventLogId(21, 40));
    }

    @Test
    public void currentLogId_fullLogWithOneArchived_currentLogIsEmpty() {
        when(storedDomainEventRepository.findHighestDomainEventId()).thenReturn(40L);
        var currentLogId = domainEventLog.currentLogId();
        assertThat(currentLogId).isEqualTo(new DomainEventLogId(41, 60));
    }

    @Test
    public void previousLogId_firstLogId_noIdReturned() {
        var previousLogId = domainEventLog.previousLogId(new DomainEventLogId(1, 20));
        assertThat(previousLogId).isEmpty();
    }

    @Test
    public void previousLogId_secondLogId_previousIdReturned() {
        var previousLogId = domainEventLog.previousLogId(new DomainEventLogId(21, 40));
        assertThat(previousLogId).contains(new DomainEventLogId(1, 20));
    }

    @Test
    public void nextLogId_firstLogId_nextIdReturned() {
        var nextId = domainEventLog.nextLogId(new DomainEventLogId(1, 20));
        assertThat(nextId).isEqualTo(new DomainEventLogId(21, 40));
    }

    @Test
    public void append() throws Exception {
        var event = new TestEvent(Instant.now());
        when(objectMapper.writeValueAsString(event)).thenReturn("test-event-as-json");
        domainEventLog.append(event);
        var storedDomainEvent = ArgumentCaptor.forClass(StoredDomainEvent.class);
        verify(storedDomainEventRepository, Mockito.only()).saveAndFlush(storedDomainEvent.capture());
        assertThat(storedDomainEvent.getValue().occurredOn()).isEqualTo(event.occurredOn);
        assertThat(storedDomainEvent.getValue().domainEventClass()).isEqualTo(TestEvent.class);
        assertThat(storedDomainEvent.getValue().toJsonString()).isEqualTo("test-event-as-json");
    }

    public static class TestEvent implements DomainEvent {

        private final Instant occurredOn;

        TestEvent(Instant occurredOn) {
            this.occurredOn = occurredOn;
        }

        @Override
        public Instant occurredOn() {
            return occurredOn;
        }
    }
}
