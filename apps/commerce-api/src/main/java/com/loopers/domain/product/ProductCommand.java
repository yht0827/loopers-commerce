package com.loopers.domain.product;

import org.springframework.data.domain.Pageable;

public record ProductCommand() {

	public record GetProductList(Long brandId, Pageable pageable) {

	}

	public record DeductStock(Long productId, Long quantity) {
	}
}
