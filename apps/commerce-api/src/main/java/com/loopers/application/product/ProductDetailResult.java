package com.loopers.application.product;

import com.loopers.domain.product.ProductInfo;

public record ProductDetailResult(
	Long productId, String productName, Long price, Long quantity, String bradName, Long likeCount) {
	public static ProductDetailResult from(ProductInfo productInfo) {
		return new ProductDetailResult(productInfo.productId(), productInfo.productName(), productInfo.price(),
			productInfo.quantity(), productInfo.bradName(), productInfo.likeCount());
	}
}
