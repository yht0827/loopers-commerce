package com.loopers.interfaces.api.product;

import java.util.List;

import com.loopers.application.product.ProductCriteria;
import com.loopers.application.product.ProductDetailResult;
import com.loopers.application.product.ProductListResult;
import com.loopers.domain.common.Price;
import com.loopers.domain.common.Quantity;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductName;
import com.loopers.domain.product.ProductSortType;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDto() {

	public record V1() {

		public record ProductRequest(
			@PositiveOrZero Integer page,
			@Positive Integer size,
			Long brandId,
			ProductSortType sortType
		) {
			public ProductCriteria.GetProductList toCriteria() {
				return new ProductCriteria.GetProductList(
					page != null ? page : 0,
					size != null ? size : 20,
					brandId,
					sortType != null ? sortType : ProductSortType.LATEST
				);
			}
		}

		public record ProductDetailResponse(Long productId, ProductName productName, Price price, LikeCount likeCount,
											Quantity quantity) {
			public static ProductDetailResponse from(
				final ProductDetailResult productDetailResult) {
				return new ProductDetailResponse(productDetailResult.productId(),
					productDetailResult.productName(),
					productDetailResult.price(), productDetailResult.likeCount(), productDetailResult.quantity());
			}
		}

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

	}
}
