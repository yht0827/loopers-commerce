package com.loopers.infrastructure.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.product.entity.Stock;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
}
