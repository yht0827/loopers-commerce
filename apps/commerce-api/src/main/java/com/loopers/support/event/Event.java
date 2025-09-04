package com.loopers.support.event;

import java.time.LocalDateTime;

public interface Event {
	String getAggregateId();

	LocalDateTime getOccurredAt();

	String getEventType();

	String getCorrelationId();
}
