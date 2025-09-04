package com.loopers.domain.product;

import com.querydsl.core.annotations.QueryProjection;

public record ProductInfo(Long productId, String productName, Long price, Long quantity, String bradName, Long likeCount) {

	@QueryProjection
	public ProductInfo {
	}
}
