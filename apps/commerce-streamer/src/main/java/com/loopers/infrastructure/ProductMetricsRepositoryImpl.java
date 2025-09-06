package com.loopers.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.ProductMetrics;
import com.loopers.domain.ProductMetricsRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductMetricsRepositoryImpl implements ProductMetricsRepository {
	private final ProductMetricsJpaRepository productMetricsJpaRepository;

	@Override
	public Optional<ProductMetrics> findByProductId(Long productId) {
		return productMetricsJpaRepository.findByProductId(productId);
	}

	@Override
	public void save(final ProductMetrics productMetrics) {
		productMetricsJpaRepository.save(productMetrics);
	}
}
