package com.loopers.infrastructure.event;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.loopers.domain.event.EventLog;
import com.loopers.domain.event.EventLogRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventLogRepositoryImpl implements EventLogRepository {

	private final EventLogJpaRepository eventLogJpaRepository;

	@Override
	public void save(EventLog eventLog) {
		eventLogJpaRepository.save(eventLog);
	}

	@Override
	public List<EventLog> findByAggregateId(String aggregateId) {
		return eventLogJpaRepository.findByAggregateIdOrderByOccurredAtDesc(aggregateId);
	}

	@Override
	public List<EventLog> findByEventType(String eventType) {
		return eventLogJpaRepository.findByEventTypeOrderByOccurredAtDesc(eventType);
	}

	@Override
	public List<EventLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		return eventLogJpaRepository.findByOccurredAtBetweenOrderByOccurredAtDesc(startDate, endDate);
	}
}
