package com.loopers.domain.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
	Optional<Product> findById(final Long id);

	Optional<Product> findByIdWithPessimisticLock(Long id);

	Optional<Product> findByIdWithOptimisticLock(Long id);

	Page<Product> getProductList(final Long brandId, final Pageable pageable);

	Product save(final Product product);
}

