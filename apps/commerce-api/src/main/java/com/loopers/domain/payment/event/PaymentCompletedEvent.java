package com.loopers.domain.payment.event;

import java.time.LocalDateTime;

public record PaymentCompletedEvent(
	String orderId,
	String userId,
	String transactionKey,
	LocalDateTime occurredAt
) implements PaymentEvent {

	public static final String EVENT_TYPE = "PAYMENT_COMPLETED";

	public static PaymentCompletedEvent create(String orderId, String userId, String transactionKey) {
		return new PaymentCompletedEvent(orderId, userId, transactionKey, LocalDateTime.now());
	}

	@Override
	public String getAggregateId() {
		return transactionKey;
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
	public String getOrderId() {
		return orderId;
	}
}
