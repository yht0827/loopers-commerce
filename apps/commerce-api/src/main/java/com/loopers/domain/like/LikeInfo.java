package com.loopers.domain.like;

public record LikeInfo(Long userId, Long productId) {

	public static LikeInfo from(Like like) {
		return new LikeInfo(like.getTargetId().getUserId(), like.getTargetId().getTargetId());
	}
}
