package com.loopers.domain.product;

import com.loopers.domain.common.BrandId;
import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;

public record ProductInfo(Long productId, ProductName productName, Price price, LikeCount likeCount,
						  Quantity quantity, BrandId brandId) {
	public static ProductInfo from(Product product) {
		return new ProductInfo(product.getId(), product.getName(), product.getPrice(), product.getLikeCount(),
			product.getQuantity(), product.getBrandId());
	}
}
