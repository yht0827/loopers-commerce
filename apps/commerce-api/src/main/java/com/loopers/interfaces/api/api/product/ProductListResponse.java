package com.loopers.interfaces.api.api.product;

import java.util.List;

import com.loopers.application.product.ProductListResult;
import com.loopers.domain.product.ProductInfo;

public record ProductListResponse(
	List<ProductInfo> products,
	Integer totalPages,
	Long totalElements
) {

	public static ProductListResponse from(final ProductListResult productListResult) {
		return new ProductListResponse(
			productListResult.products(),
			productListResult.totalPages(),
			productListResult.totalElements()
		);
	}
}
