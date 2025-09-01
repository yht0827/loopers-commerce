package com.loopers.support.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class DomainApplicationEvent<T extends Event> extends ApplicationEvent {
	private final EventEnvelope<T> envelope;

	public DomainApplicationEvent(Object source, EventEnvelope<T> envelope) {
		super(source);
		this.envelope = envelope;
	}

	public String getEventId() {
		return envelope.getEventId();
	}

	public String getEventType() {
		return envelope.getEventType();
	}

	public LocalDateTime getOccurredAt() {
		return envelope.getOccurredAt();
	}

	public String getAggregateId() {
		return envelope.getAggregateId();
	}

	public String getCorrelationId() {
		return envelope.getCorrelationId();
	}

	public T getPayload() {
		return envelope.getPayload();
	}

	public boolean isPayloadOfType(Class<?> type) {
		return type.isInstance(envelope.getPayload());
	}
}
