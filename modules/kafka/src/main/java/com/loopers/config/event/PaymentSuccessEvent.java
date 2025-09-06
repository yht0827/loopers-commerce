package com.loopers.config.event;

import java.time.LocalDateTime;

public record PaymentSuccessEvent(
	String orderId,
	Long amount,
	LocalDateTime occurredAt,
	String eventId
) {

	public static PaymentSuccessEvent create(String orderId, Long amount) {
		return new PaymentSuccessEvent(
			orderId,
			amount,
			LocalDateTime.now(),
			"payment-success-" + orderId + "-" + System.currentTimeMillis());
	}
}
