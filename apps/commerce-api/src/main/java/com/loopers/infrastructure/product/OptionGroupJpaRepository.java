package com.loopers.infrastructure.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.product.entity.OptionGroup;

public interface OptionGroupJpaRepository extends JpaRepository<OptionGroup, Long> {
}
