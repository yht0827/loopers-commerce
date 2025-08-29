package com.loopers.infrastructure.event;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.loopers.domain.event.EventAuditLog;
import com.loopers.domain.event.EventAuditLogRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventAuditLogRepositoryImpl implements EventAuditLogRepository {

	private final EventAuditLogJpaRepository eventAuditLogJpaRepository;

	@Override
	public void save(EventAuditLog eventAuditLog) {
		eventAuditLogJpaRepository.save(eventAuditLog);
	}

	@Override
	public List<EventAuditLog> findByAggregateId(String aggregateId) {
		return eventAuditLogJpaRepository.findByAggregateIdOrderByOccurredAtDesc(aggregateId);
	}

	@Override
	public List<EventAuditLog> findByEventType(String eventType) {
		return eventAuditLogJpaRepository.findByEventTypeOrderByOccurredAtDesc(eventType);
	}

	@Override
	public List<EventAuditLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		return eventAuditLogJpaRepository.findByOccurredAtBetweenOrderByOccurredAtDesc(startDate, endDate);
	}
}