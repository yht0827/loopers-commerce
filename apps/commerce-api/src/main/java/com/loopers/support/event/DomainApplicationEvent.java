package com.loopers.support.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.Getter;

@Getter
public class DomainApplicationEvent extends ApplicationEvent {
	private final EventEnvelope<?> envelope;

	public DomainApplicationEvent(Object source, EventEnvelope<?> envelope) {
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

	@SuppressWarnings("unchecked")
	public <T> T getPayload(Class<T> type) {
		Object payload = envelope.getPayload();
		if (type.isInstance(payload)) {
			return (T)payload;
		}
		throw new CoreException(ErrorType.NOT_FOUND, "페이 로드 타입이 아닙니다.: " + type.getName());
	}

	public boolean isPayloadOfType(Class<?> type) {
		return !type.isInstance(envelope.getPayload());
	}
}
