package com.loopers.domain.product;

import org.springframework.stereotype.Service;

import com.loopers.domain.product.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Product findById(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));
	}
}

