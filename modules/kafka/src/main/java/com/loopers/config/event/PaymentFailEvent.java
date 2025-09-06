package com.loopers.config.event;

import java.time.LocalDateTime;

public record PaymentFailEvent(
	String orderId,
	Long amount,
	String failureReason,
	LocalDateTime occurredAt,
	String eventId
) {

	public static PaymentFailEvent create(String orderId, Long amount, String failureReason) {
		return new PaymentFailEvent(
			orderId,
			amount,
			failureReason,
			LocalDateTime.now(),
			"payment-fail-" + orderId + "-" + System.currentTimeMillis());
	}
}
