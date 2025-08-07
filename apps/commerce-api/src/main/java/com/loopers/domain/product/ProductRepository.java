package com.loopers.domain.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.loopers.domain.common.BrandId;

public interface ProductRepository {
	Optional<Product> findById(final Long id);

	Page<Product> getProductList(final BrandId brandId, final Pageable pageable);
}

