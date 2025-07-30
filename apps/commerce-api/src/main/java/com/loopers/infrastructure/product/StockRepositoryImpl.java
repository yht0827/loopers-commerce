package com.loopers.infrastructure.product;

import org.springframework.stereotype.Repository;

import com.loopers.domain.product.StockRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {
	private final StockJpaRepository stockJpaRepository;
}
