package com.loopers.infrastructure.product;

import org.springframework.stereotype.Repository;

import com.loopers.domain.product.OptionValueRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OptionValueRepositoryImpl implements OptionValueRepository {
	private final OptionValueJpaRepository optionValueJpaRepository;
}
