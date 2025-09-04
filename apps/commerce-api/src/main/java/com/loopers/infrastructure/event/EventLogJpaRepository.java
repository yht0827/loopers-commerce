package com.loopers.infrastructure.event;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.loopers.domain.event.EventLog;

public interface EventLogJpaRepository extends JpaRepository<EventLog, Long> {

	@Query("SELECT e FROM EventLog e WHERE e.aggregateId.aggregateId = :aggregateId ORDER BY e.occurredAt DESC")
	List<EventLog> findByAggregateIdOrderByOccurredAtDesc(@Param("aggregateId") String aggregateId);

	@Query("SELECT e FROM EventLog e WHERE e.eventType.eventType = :eventType ORDER BY e.occurredAt DESC")
	List<EventLog> findByEventTypeOrderByOccurredAtDesc(@Param("eventType") String eventType);

	@Query("SELECT e FROM EventLog e WHERE e.occurredAt BETWEEN :startDate AND :endDate ORDER BY e.occurredAt DESC")
	List<EventLog> findByOccurredAtBetweenOrderByOccurredAtDesc(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
