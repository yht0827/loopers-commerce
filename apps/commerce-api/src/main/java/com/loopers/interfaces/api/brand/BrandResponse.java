package com.loopers.interfaces.api.brand;

import java.util.List;

import com.loopers.application.brand.BrandResult;
import com.loopers.domain.brand.BrandName;
import com.loopers.domain.product.ProductInfo;

public record BrandResponse(
	Long brandId,
	BrandName brandName,
	List<ProductInfo> products,
	Integer totalPages,
	Long totalElements
) {
	public static BrandResponse from(BrandResult brandResult) {
		return new BrandResponse(
			brandResult.brandId(),
			brandResult.brandName(),
			brandResult.products(),
			brandResult.totalPages(),
			brandResult.totalElements()
		);
	}
}
