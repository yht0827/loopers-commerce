package com.loopers.domain.event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAuditLogRepository {

	void save(EventAuditLog eventAuditLog);

	List<EventAuditLog> findByAggregateId(String aggregateId);

	List<EventAuditLog> findByEventType(String eventType);

	List<EventAuditLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
