package com.loopers.domain.brand;

import java.util.Optional;

import com.loopers.domain.common.BrandId;

public interface BrandRepository {

	Optional<Brand> findById(final BrandId id);

}
