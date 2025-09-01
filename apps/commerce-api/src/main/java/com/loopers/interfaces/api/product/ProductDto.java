package com.loopers.interfaces.api.product;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.loopers.application.product.ProductCriteria;
import com.loopers.application.product.ProductDetailResult;
import com.loopers.application.product.ProductListResult;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductSortType;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDto() {

	public record V1() {

		public record ProductRequest(
			@PositiveOrZero Integer page,
			@Positive Integer size,
			Long brandId,
			String sort
		) {

			public ProductCriteria.GetProductList toCriteria() {
				return new ProductCriteria.GetProductList(
					brandId,
					PageRequest.of(
						page != null ? page : 0,
						size != null ? size : 10,
						(sort != null ? ProductSortType.fromRequestValue(sort) : ProductSortType.LATEST).getSort()
					));
			}
		}

		public record ProductDetailResponse(Long productId, String productName, Long price, Long quantity, String bradName,
											Long likeCount) {
			public static ProductDetailResponse from(final ProductDetailResult productDetailResult) {
				return new ProductDetailResponse(
					productDetailResult.productId(), productDetailResult.productName(), productDetailResult.price(),
					productDetailResult.quantity(), productDetailResult.bradName(), productDetailResult.likeCount());
			}
		}

		public record ProductListResponse(List<ProductInfo> products, Integer totalPages, Long totalElements) {

			public static ProductListResponse from(final ProductListResult productListResult) {
				return new ProductListResponse(productListResult.products(), productListResult.totalPages(),
					productListResult.totalElements());
			}
		}

	}
}
