package com.loopers.infrastructure.product;

import org.springframework.stereotype.Repository;

import com.loopers.domain.product.StockHistoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockHistoryRepositoryImpl implements StockHistoryRepository {
	private final StockHistoryJpaRepository stockHistoryJpaRepository;
}
