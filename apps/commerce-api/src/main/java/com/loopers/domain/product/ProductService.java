package com.loopers.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Page<ProductInfo> getProductList(final Long brandId, final Pageable pageable) {
		Page<Product> products = productRepository.getProductList(brandId, pageable);
		return products.map(ProductInfo::from);
	}

	public ProductInfo getProductDetail(final Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("해당 [id = " + productId + "]의 상품을 찾을 수 없습니다."));

		return ProductInfo.from(product);
	}
}

