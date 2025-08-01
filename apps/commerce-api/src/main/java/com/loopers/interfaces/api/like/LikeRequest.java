package com.loopers.interfaces.api.like;

import com.loopers.domain.like.LikeCommand;

public record LikeRequest(
	Long userId,
	Long productId
) {

	public LikeCommand toCommand() {
		return new LikeCommand(userId, productId);
	}
}
