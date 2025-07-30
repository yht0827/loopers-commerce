package com.loopers.domain.product;

import java.util.Optional;

import com.loopers.domain.product.entity.Product;

public interface ProductRepository {
	Optional<Product> findById(final Long id);
}

