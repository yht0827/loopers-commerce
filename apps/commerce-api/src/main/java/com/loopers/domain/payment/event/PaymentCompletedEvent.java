package com.loopers.domain.payment.event;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record PaymentCompletedEvent(
	String orderId,
	String transactionKey,
	LocalDateTime occurredAt
) implements PaymentEvent {

	public static final String EVENT_TYPE = "PAYMENT_COMPLETED";

	public static PaymentCompletedEvent create(String orderId, String transactionKey) {
		return PaymentCompletedEvent.builder()
			.orderId(orderId)
			.transactionKey(transactionKey)
			.occurredAt(LocalDateTime.now())
			.build();
	}

	@Override
	public String getAggregateId() {
		return orderId;
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
		return orderId;
	}

	@Override
	public String getOrderId() {
		return orderId;
	}
}
