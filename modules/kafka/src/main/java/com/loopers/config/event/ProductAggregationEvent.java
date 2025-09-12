package com.loopers.config.event;

import static com.loopers.config.event.ProductEventType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductAggregationEvent(
	Long productId,
	String userId,
	String action, // "LIKE" or "UNLIKE"
	ProductEventType eventType,
	LocalDate eventDate,
	LocalDateTime occurredAt,
	String eventId
) {

	public static ProductAggregationEvent createLike(Long productId, String userId) {
		return new ProductAggregationEvent(
			productId,
			userId,
			"LIKE",
			LIKE,
			LocalDate.now(),
			LocalDateTime.now(),
			"like-agg-" + productId + "-" + System.currentTimeMillis()
		);
	}

	public static ProductAggregationEvent createUnlike(Long productId, String userId) {
		return new ProductAggregationEvent(
			productId,
			userId,
			"UNLIKE",
			LIKE,
			LocalDate.now(),
			LocalDateTime.now(),
			"like-agg-" + productId + "-" + System.currentTimeMillis()
		);
	}

	/**
	 * LIKE/UNLIKE 구분을 위한 점수 배수
	 */
	public double getScoreMultiplier() {
		return switch (action) {
			case "LIKE" -> 1.0;   // 양수
			case "UNLIKE" -> -1.0; // 음수
			default -> 0.0;
		};
	}
}
