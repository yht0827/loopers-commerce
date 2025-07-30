package com.loopers.domain.brand;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BrandService {
	private final BrandRepository brandRepository;
}
