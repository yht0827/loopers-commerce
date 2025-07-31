package com.loopers.domain.product;

public record ProductInfo(Long productId, ProductName productName) {
	public static ProductInfo from(Product product) {
		return new ProductInfo(product.getId(), product.getName());
	}
}
