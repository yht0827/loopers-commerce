package com.loopers.infrastructure.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.product.entity.SkuOptionValue;
import com.loopers.domain.product.entity.vo.SkuOptionValueId;

public interface SkuOptionValueJpaRepository extends JpaRepository<SkuOptionValue, SkuOptionValueId> {
}
