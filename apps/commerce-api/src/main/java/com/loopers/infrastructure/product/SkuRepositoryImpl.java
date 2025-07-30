package com.loopers.infrastructure.product;

import org.springframework.stereotype.Repository;

import com.loopers.domain.product.SkuRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SkuRepositoryImpl implements SkuRepository {
	private final SkuJpaRepository skuJpaRepository;
}
