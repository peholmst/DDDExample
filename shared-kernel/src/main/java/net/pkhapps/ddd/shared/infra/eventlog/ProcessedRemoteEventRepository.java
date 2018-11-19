package net.pkhapps.ddd.shared.infra.eventlog;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TODO document me
 */
interface ProcessedRemoteEventRepository extends JpaRepository<ProcessedRemoteEvent, String> {
}
