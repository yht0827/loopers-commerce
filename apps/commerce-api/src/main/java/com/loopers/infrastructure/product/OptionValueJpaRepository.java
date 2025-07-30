package com.loopers.infrastructure.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.product.entity.OptionValue;

public interface OptionValueJpaRepository extends JpaRepository<OptionValue, Long> {
}
