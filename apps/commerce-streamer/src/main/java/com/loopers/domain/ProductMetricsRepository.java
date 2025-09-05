package com.loopers.domain;

import java.util.Optional;

public interface ProductMetricsRepository {

	Optional<ProductMetrics> findByProductId(Long productId);

	void save(ProductMetrics productMetrics);
}
