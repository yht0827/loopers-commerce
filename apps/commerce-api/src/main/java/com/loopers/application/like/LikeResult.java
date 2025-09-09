package com.loopers.application.like;

import com.loopers.domain.like.Like;

public record LikeResult(String userId, Long productId) {

	public static LikeResult from(Like like) {

		return new LikeResult(like.getUserId().getUserId(), like.getProductId().getProductId());
	}

}
