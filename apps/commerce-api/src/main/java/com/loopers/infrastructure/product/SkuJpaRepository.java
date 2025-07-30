package com.loopers.infrastructure.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.product.entity.Sku;

public interface SkuJpaRepository extends JpaRepository<Sku, Long> {
}
