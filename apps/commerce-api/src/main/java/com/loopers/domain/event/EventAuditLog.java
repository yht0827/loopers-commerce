package com.loopers.domain.event;

import java.time.LocalDateTime;

import com.loopers.domain.BaseEntity;
import com.loopers.support.event.DomainApplicationEvent;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_audit_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventAuditLog extends BaseEntity {

	private EventId eventId;
	private EventType eventType;
	private AggregateId aggregateId;
	private CorrelationId correlationId;
	private EventPayload payload;
	private LocalDateTime occurredAt;

	public EventAuditLog(EventId eventId, EventType eventType, AggregateId aggregateId, LocalDateTime occurredAt) {
		this.eventId = eventId;
		this.eventType = eventType;
		this.aggregateId = aggregateId;
		this.occurredAt = occurredAt;
	}

	public EventAuditLog(EventId eventId, EventType eventType, AggregateId aggregateId,
		CorrelationId correlationId, EventPayload payload, LocalDateTime occurredAt) {
		this.eventId = eventId;
		this.eventType = eventType;
		this.aggregateId = aggregateId;
		this.correlationId = correlationId;
		this.payload = payload;
		this.occurredAt = occurredAt;
	}

	public static EventAuditLog from(DomainApplicationEvent event) {
		return new EventAuditLog(
			new EventId(event.getEventId()),
			new EventType(event.getEventType()),
			new AggregateId(event.getAggregateId()),
			event.getOccurredAt()
		);
	}

	public static EventAuditLog withPayload(DomainApplicationEvent event, String payloadJson) {
		return new EventAuditLog(
			new EventId(event.getEventId()),
			new EventType(event.getEventType()),
			new AggregateId(event.getAggregateId()),
			null,
			new EventPayload(payloadJson),
			event.getOccurredAt()
		);
	}
}
