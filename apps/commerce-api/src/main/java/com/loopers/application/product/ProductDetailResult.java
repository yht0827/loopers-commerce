package com.loopers.application.product;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandName;
import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductName;

public record ProductDetailResult(
	Long productId, ProductName productName, Price price, LikeCount likeCount, Quantity quantity,
	Long brandId, BrandName brandName) {
	public static ProductDetailResult from(ProductInfo productInfo, BrandInfo brandInfo) {
		return new ProductDetailResult(productInfo.productId(), productInfo.productName(), productInfo.price(),
			productInfo.likeCount(), productInfo.quantity(), brandInfo.brandId(), brandInfo.brandName());
	}
}
