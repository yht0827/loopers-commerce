package com.loopers.domain.event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventLogRepository {

	void save(EventLog eventLog);

	List<EventLog> findByAggregateId(String aggregateId);

	List<EventLog> findByEventType(String eventType);

	List<EventLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
