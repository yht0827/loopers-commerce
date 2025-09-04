package com.loopers.domain.event;

import java.time.LocalDateTime;

import com.loopers.domain.BaseEntity;
import com.loopers.support.event.Envelope;
import com.loopers.support.event.Event;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventLog extends BaseEntity {

	private EventId eventId;
	private EventType eventType;
	private AggregateId aggregateId;
	private CorrelationId correlationId;
	private EventPayload payload;
	private LocalDateTime occurredAt;

	public EventLog(EventId eventId, EventType eventType, AggregateId aggregateId,
		CorrelationId correlationId, EventPayload payload, LocalDateTime occurredAt) {
		this.eventId = eventId;
		this.eventType = eventType;
		this.aggregateId = aggregateId;
		this.correlationId = correlationId;
		this.payload = payload;
		this.occurredAt = occurredAt;
	}

	public static <T extends Event> EventLog from(Envelope<T> event) {
		return new EventLog(
			new EventId(event.getEventId()),
			new EventType(event.getEventType()),
			new AggregateId(event.getAggregateId()),
			new CorrelationId(event.getCorrelationId()),
			null,
			event.getOccurredAt()
		);
	}
}
