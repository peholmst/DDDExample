package net.pkhapps.ddd.shared.infra.eventlog;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link ProcessedRemoteEvent}.
 */
interface ProcessedRemoteEventRepository extends JpaRepository<ProcessedRemoteEvent, String> {
}
