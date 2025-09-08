package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeResult;

public record LikeResponse(String userId, Long productId) {

	public static LikeResponse from(LikeResult likeResult) {
		return new LikeResponse(likeResult.userId(), likeResult.productId());
	}
}
