package com.loopers.interfaces.api.product;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.product.ProductDetailResult;
import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductListResult;
import com.loopers.domain.product.ProductSortType;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductV1Controller {

	private final ProductFacade productFacade;

	@GetMapping
	public ApiResponse<ProductListResponse> getProductList(
		@RequestParam(required = false) Long brandId,
		@RequestParam(defaultValue = "latest") String sort,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size) {

		ProductSortType sortType = ProductSortType.from(sort);
		Pageable pageable = PageRequest.of(page, size, sortType.getSort());

		ProductListResult products = productFacade.getProductList(brandId, pageable);
		ProductListResponse response = ProductListResponse.from(products);
		return ApiResponse.success(response);
	}

	@GetMapping("/{productId}")
	public ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable final Long productId) {
		ProductDetailResult product = productFacade.getProductDetail(productId);
		ProductDetailResponse response = ProductDetailResponse.from(product);
		return ApiResponse.success(response);
	}
}
