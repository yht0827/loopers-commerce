package com.loopers.application.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductSortType;
import com.loopers.interfaces.api.product.ProductRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProductFacade {

	private final ProductService productService;

	public ProductListResult getProductList(final ProductRequest productRequest, final Pageable pageable) {
		ProductSortType sortType = ProductSortType.from(productRequest.sort());
		Page<ProductInfo> products = productService.getProductList(
			PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortType.getSort()));
		return ProductListResult.from(products);
	}

	public ProductDetailResult getProductById(final Long productId) {
		ProductInfo productInfo = productService.findById(productId);
		return ProductDetailResult.from(productInfo);
	}

}
