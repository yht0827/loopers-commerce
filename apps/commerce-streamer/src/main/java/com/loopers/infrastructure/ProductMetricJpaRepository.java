package com.loopers.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.ProductMetric;

public interface ProductMetricJpaRepository extends JpaRepository<ProductMetric, Long> {

	Optional<ProductMetric> findByProductId(Long productId);
}
