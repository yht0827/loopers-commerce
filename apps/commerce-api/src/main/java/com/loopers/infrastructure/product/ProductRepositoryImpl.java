package com.loopers.infrastructure.product;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.entity.Product;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
	private final ProductJpaRepository productJpaRepository;

	@Override
	public Optional<Product> findById(final Long id) {
		return productJpaRepository.findById(id);
	}
}
