package com.loopers.infrastructure.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.product.entity.StockHistory;

public interface StockHistoryJpaRepository extends JpaRepository<StockHistory, Long> {
}
