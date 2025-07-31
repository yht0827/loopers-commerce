package com.loopers.application.product;

import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductName;

public record ProductDetailResult(Long productId, ProductName productName) {
	public static ProductDetailResult from(ProductInfo productInfo) {
		return new ProductDetailResult(productInfo.productId(), productInfo.productName());
	}
}
