package com.loopers.domain.platform.event;

import java.time.LocalDateTime;

import com.loopers.support.event.Event;

public record DataPlatformEvent(
	String dataType,
	String aggregateId,
	LocalDateTime occurredAt
) implements Event {

	public static final String EVENT_TYPE = "DATA_PLATFORM_TRANS";

	public static DataPlatformEvent create(String dataType, String aggregateId) {
		return new DataPlatformEvent(dataType, aggregateId, LocalDateTime.now());
	}

	@Override
	public String getAggregateId() {
		return aggregateId;
	}

	@Override
	public LocalDateTime getOccurredAt() {
		return occurredAt;
	}

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

	@Override
	public String getCorrelationId() {
		return "";
	}
}
