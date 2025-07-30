package com.loopers.infrastructure.product;

import org.springframework.stereotype.Repository;

import com.loopers.domain.product.OptionGroupRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OptionGroupRepositoryImpl implements OptionGroupRepository {
	private final OptionGroupJpaRepository optionGroupJpaRepository;
}
