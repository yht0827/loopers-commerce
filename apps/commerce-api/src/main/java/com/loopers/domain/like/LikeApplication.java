package com.loopers.domain.like;

public record LikeApplication(Long userId, Long targetId) {

	public Like toEntity() {
		LikeId likeId = new LikeId(userId, targetId);
		return new Like(likeId);
	}
}
