package com.loopers.application.brand;

import java.util.List;

import org.springframework.data.domain.Page;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandName;
import com.loopers.domain.product.ProductInfo;

public record BrandResult(Long brandId, BrandName brandName, List<ProductInfo> products, Integer totalPages, Long totalElements) {

	public static BrandResult from(BrandInfo brand, Page<ProductInfo> products) {
		return new BrandResult(brand.brandId(), brand.brandName(), products.getContent(), products.getTotalPages(),
			products.getTotalElements());
	}
}
