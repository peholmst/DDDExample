package net.pkhapps.ddd.shared.infra.eventlog;

import org.springframework.lang.NonNull;

/**
 * TODO Document me
 */
public interface RemoteEventLogService {

    @NonNull
    String source();

    @NonNull
    RemoteEventLog currentLog();
}
