package com.loopers.config.event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductLikeAggregationEvent(
	Long productId,
	String userId,
	String action, // "LIKE" or "UNLIKE"
	LocalDate eventDate,
	LocalDateTime occurredAt,
	String eventId
) {

	public static ProductLikeAggregationEvent createLike(Long productId, String userId) {
		return new ProductLikeAggregationEvent(
			productId,
			userId,
			"LIKE",
			LocalDate.now(),
			LocalDateTime.now(),
			"like-agg-" + productId + "-" + System.currentTimeMillis()
		);
	}

	public static ProductLikeAggregationEvent createUnlike(Long productId, String userId) {
		return new ProductLikeAggregationEvent(
			productId,
			userId,
			"UNLIKE",
			LocalDate.now(),
			LocalDateTime.now(),
			"like-agg-" + productId + "-" + System.currentTimeMillis()
		);
	}
}
