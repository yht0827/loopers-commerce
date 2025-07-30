package com.loopers.domain.brand;

import java.util.Optional;

import com.loopers.domain.brand.entity.Brand;

public interface BrandRepository {

	Optional<Brand> findById(final Long id);

}
