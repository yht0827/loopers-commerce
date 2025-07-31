package com.loopers.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.loopers.application.product.ProductCriteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Page<ProductInfo> getProductList(final Pageable pageable) {
		Page<Product> products = productRepository.getProductList(pageable);
		return products.map(ProductInfo::from);
	}

	public ProductInfo findById(final Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("해당 [id = " + productId + "]의 상품을 찾을 수 없습니다."));

		return ProductInfo.from(product);
	}

	public Page<ProductInfo> getProductsByBrandId(final Long brandId, final Pageable pageable) {
		Page<Product> products = productRepository.findByBrandId(brandId, pageable);
		return products.map(ProductInfo::from);
	}
}

