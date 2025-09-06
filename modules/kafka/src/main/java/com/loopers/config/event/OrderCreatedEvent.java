package com.loopers.config.event;

import java.time.LocalDateTime;

public record OrderCreatedEvent(
	String userId,
	String orderId,
	Long totalAmount,
	LocalDateTime occurredAt,
	String eventId
) {

	public static OrderCreatedEvent create(String userId, String orderId, Long totalAmount) {
		return new OrderCreatedEvent(
			userId,
			orderId,
			totalAmount,
			LocalDateTime.now(),
			"order-created-" + orderId + "-" + System.currentTimeMillis());
	}
}
