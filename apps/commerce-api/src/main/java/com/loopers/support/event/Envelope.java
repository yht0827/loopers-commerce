package com.loopers.support.event;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class Envelope<T extends Event> extends ApplicationEvent {
	private final String eventId;
	private final LocalDateTime occurredAt;
	private final String eventType;
	private final T payload;
	private final String correlationId;
	private final String aggregateId;

	public Envelope(T payload) {
		super(payload);
		this.eventId = UUID.randomUUID().toString();
		this.occurredAt = payload.getOccurredAt();
		this.eventType = payload.getEventType();
		this.payload = payload;
		this.correlationId = payload.getCorrelationId();
		this.aggregateId = payload.getAggregateId();
	}

	public static <T extends Event> Envelope<T> of(T payload) {
		return new Envelope<>(payload);
	}
}
