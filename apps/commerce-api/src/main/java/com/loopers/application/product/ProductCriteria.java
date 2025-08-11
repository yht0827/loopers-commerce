package com.loopers.application.product;

import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductSortType;

public record ProductCriteria() {

	public record GetProductList(
		int page,
		int size,
		Long brandId,
		ProductSortType sortType
	) {

		public ProductCommand.GetProductList toCommand() {
			return new ProductCommand.GetProductList(
				page,
				size,
				brandId,
				sortType != null ? sortType : ProductSortType.LATEST
			);
		}
	}
}
