package com.loopers.domain.product;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkuOptionValueService {

	private final SkuOptionValueRepository skuOptionValueRepository;
}

