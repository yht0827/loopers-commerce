package com.loopers.domain.payment.event;

import java.time.LocalDateTime;

public record PaymentFailedEvent(
	String orderId,
	String userId,
	String transactionKey,
	Long amount,
	String reason,
	LocalDateTime occurredAt,
	String errorCode
) implements PaymentEvent {

	public static final String EVENT_TYPE = "PAYMENT_FAILED";

	public static PaymentFailedEvent create(String orderId, String userId, String transactionKey,
		Long amount, String reason, String errorCode) {
		return new PaymentFailedEvent(orderId, userId, transactionKey, amount, reason, LocalDateTime.now(), errorCode);
	}

	public static PaymentFailedEvent create(String orderId, String userId, String transactionKey,
		Long amount, Exception exception) {
		String failureReason = exception.getMessage();
		String errorCode = exception.getClass().getSimpleName();
		return create(orderId, userId, transactionKey, amount, failureReason, errorCode);
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
