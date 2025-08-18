package com.loopers.application.product;

import org.springframework.data.domain.Pageable;

import com.loopers.domain.product.ProductCommand;

public record ProductCriteria() {

	public record GetProductList(
		Long brandId,
		Pageable pageable
	) {

		public ProductCommand.GetProductList toCommand() {
			return new ProductCommand.GetProductList(brandId, pageable);
		}
	}
}
