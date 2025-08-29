package com.loopers.support.event;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class EventEnvelope<T extends Event> {
	private final String eventId;
	private final LocalDateTime occurredAt;
	private final String eventType;
	private final T payload;
	private final String correlationId;
	private final String aggregateId;

	public EventEnvelope(T payload) {
		this(payload, null);
	}

	public EventEnvelope(T payload, String correlationId) {
		this.eventId = UUID.randomUUID().toString();
		this.occurredAt = payload.getOccurredAt();
		this.eventType = payload.getEventType();
		this.payload = payload;
		this.correlationId = correlationId;
		this.aggregateId = payload.getAggregateId();
	}

	public static <T extends Event> EventEnvelope<T> of(T payload) {
		return new EventEnvelope<>(payload);
	}

	public static <T extends Event> EventEnvelope<T> of(T payload, String correlationId) {
		return new EventEnvelope<>(payload, correlationId);
	}

}
