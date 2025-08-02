package com.loopers.application.product;

import java.util.List;

import org.springframework.data.domain.Page;

import com.loopers.domain.product.ProductInfo;

public record ProductListResult(List<ProductInfo> products, Integer totalPages, Long totalElements) {
	public static ProductListResult from(Page<ProductInfo> productInfo) {
		return new ProductListResult(productInfo.getContent(), productInfo.getTotalPages(), productInfo.getTotalElements());
	}
}
