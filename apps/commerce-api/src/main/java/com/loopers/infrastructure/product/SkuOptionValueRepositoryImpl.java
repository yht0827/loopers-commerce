package com.loopers.infrastructure.product;

import org.springframework.stereotype.Repository;

import com.loopers.domain.product.SkuOptionValueRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SkuOptionValueRepositoryImpl implements SkuOptionValueRepository {
	private final SkuOptionValueJpaRepository skuOptionValueJpaRepository;
}
