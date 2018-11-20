package net.pkhapps.ddd.shared.infra.eventlog;

import org.springframework.lang.NonNull;

/**
 * Service interface for retrieving {@link RemoteEventLog}s from a remote event source. An instance of this interface
 * for each event source to retrieve logs from should exist in the application context.
 */
public interface RemoteEventLogService {

    /**
     * Returns the name of the remote event source (such as the URL of the server).
     */
    @NonNull
    String source();

    /**
     * Returns the current event log. Use {@link RemoteEventLog#previous()} to go back.
     */
    @NonNull
    RemoteEventLog currentLog();
}
