package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductDetailResult;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.Price;
import com.loopers.domain.product.ProductName;
import com.loopers.domain.product.Quantity;

public record ProductDetailResponse(Long productId, ProductName productName, Price price, LikeCount likeCount,
									Quantity quantity) {
	public static ProductDetailResponse from(final ProductDetailResult productDetailResult) {
		return new ProductDetailResponse(productDetailResult.productId(), productDetailResult.productName(),
			productDetailResult.price(), productDetailResult.likeCount(), productDetailResult.quantity());
	}
}
