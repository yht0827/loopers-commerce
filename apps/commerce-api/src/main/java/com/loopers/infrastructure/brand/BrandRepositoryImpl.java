package com.loopers.infrastructure.brand;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.brand.Brand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class BrandRepositoryImpl implements BrandRepository {
	private final BrandJpaRepository brandJpaRepository;

	@Override
	public Optional<Brand> findById(final Long id) {
		return brandJpaRepository.findById(id);
	}
}
