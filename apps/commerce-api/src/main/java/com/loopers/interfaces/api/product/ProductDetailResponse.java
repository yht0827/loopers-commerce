package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductDetailResult;
import com.loopers.domain.product.ProductName;

public record ProductDetailResponse(Long productId, ProductName productName) {
	public static ProductDetailResponse from(final ProductDetailResult productDetailResult) {
		return new ProductDetailResponse(productDetailResult.productId(), productDetailResult.productName());
	}
}
