package com.loopers.config.event;

import java.time.LocalDateTime;

public record ProductLikedEvent(
	String userId,
	Long productId,
	LocalDateTime occurredAt,
	String eventId
) {

	public static ProductLikedEvent create(String userId, Long productId) {
		return new ProductLikedEvent(
			userId,
			productId,
			LocalDateTime.now(),
			"product-liked-" + productId + "-" + System.currentTimeMillis());
	}
}