package com.loopers.domain.product;

public record ProductCommand() {

	public record GetProductList(
		int page,
		int size,
		Long brandId,
		ProductSortType sortType
	) {

	}
}
