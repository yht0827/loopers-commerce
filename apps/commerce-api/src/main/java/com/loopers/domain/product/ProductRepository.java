package com.loopers.domain.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
	Optional<Product> findById(final Long id);

	Page<Product> getProductList(final Pageable pageable);

	Page<Product> findByBrandId(final Long brandId, final Pageable pageable);
}

