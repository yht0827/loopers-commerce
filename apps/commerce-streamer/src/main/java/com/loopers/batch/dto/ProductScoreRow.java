package com.loopers.batch.dto;

public record ProductScoreRow(
	Long productId,
	Double score,
	Long likeCount,
	Long viewCount,
	Long orderCount
) {
}
