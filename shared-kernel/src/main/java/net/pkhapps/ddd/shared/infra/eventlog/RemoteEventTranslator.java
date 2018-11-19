package net.pkhapps.ddd.shared.infra.eventlog;

import net.pkhapps.ddd.shared.domain.base.DomainEvent;
import org.springframework.lang.NonNull;

/**
 * TODO implement me!
 */
public interface RemoteEventTranslator {

    boolean supports(@NonNull StoredDomainEvent remoteEvent);

    @NonNull
    DomainEvent translate(@NonNull StoredDomainEvent remoteEvent);
}
