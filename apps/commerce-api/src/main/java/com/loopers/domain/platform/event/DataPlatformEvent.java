package com.loopers.domain.platform.event;

import java.time.LocalDateTime;

import com.loopers.support.event.Event;

public record DataPlatformEvent(
	String dataType,
	String aggregateId,
	String payload,
	String source,
	LocalDateTime occurredAt
) implements Event {

	public static final String EVENT_TYPE = "DATA_PLATFORM_TRANS";

	public static DataPlatformEvent create(String dataType, String aggregateId, String payload, String source) {
		return new DataPlatformEvent(dataType, aggregateId, payload, source, LocalDateTime.now());
	}

	public static DataPlatformEvent fromOrder(String orderId, String orderData) {
		return create("ORDER", orderId, orderData, "ORDER_DOMAIN");
	}

	public static DataPlatformEvent fromPayment(String paymentId, String paymentData) {
		return create("PAYMENT", paymentId, paymentData, "PAYMENT_DOMAIN");
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
}
