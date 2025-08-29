package com.loopers.infrastructure.event;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.loopers.domain.event.EventAuditLog;

public interface EventAuditLogJpaRepository extends JpaRepository<EventAuditLog, Long> {

	@Query("SELECT e FROM EventAuditLog e WHERE e.aggregateId.aggregateId = :aggregateId ORDER BY e.occurredAt DESC")
	List<EventAuditLog> findByAggregateIdOrderByOccurredAtDesc(@Param("aggregateId") String aggregateId);

	@Query("SELECT e FROM EventAuditLog e WHERE e.eventType.eventType = :eventType ORDER BY e.occurredAt DESC")
	List<EventAuditLog> findByEventTypeOrderByOccurredAtDesc(@Param("eventType") String eventType);

	@Query("SELECT e FROM EventAuditLog e WHERE e.occurredAt BETWEEN :startDate AND :endDate ORDER BY e.occurredAt DESC")
	List<EventAuditLog> findByOccurredAtBetweenOrderByOccurredAtDesc(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
