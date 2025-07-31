package com.loopers.interfaces.api.api.product;

import org.springframework.data.domain.Pageable;

import com.loopers.application.product.ProductCriteria;

public record ProductRequest(
	String sort   // 정렬 기준: latest, price_asc, likes_desc
) {
	public ProductCriteria toCriteria(Pageable pageable) {
		return new ProductCriteria(sort, pageable);
	}
}
