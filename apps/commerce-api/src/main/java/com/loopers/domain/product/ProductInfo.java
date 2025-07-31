package com.loopers.domain.product;

public record ProductInfo(Long productId, ProductName productName, Price price, LikeCount likeCount,
						  Quantity quantity) {
	public static ProductInfo from(Product product) {
		return new ProductInfo(product.getId(), product.getName(), product.getPrice(), product.getLikeCount(),
			product.getQuantity());
	}
}
