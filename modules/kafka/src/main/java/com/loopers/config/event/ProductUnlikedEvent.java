package com.loopers.config.event;

import java.time.LocalDateTime;

public record ProductUnlikedEvent(
	String userId,
	Long productId,
	LocalDateTime occurredAt,
	String eventId
) {

	public static ProductUnlikedEvent create(String userId, Long productId) {
		return new ProductUnlikedEvent(
			userId,
			productId,
			LocalDateTime.now(),
			"product-unliked-" + productId + "-" + System.currentTimeMillis());
	}
}