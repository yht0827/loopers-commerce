package com.loopers.infrastructure.product;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.product.ProductAggregate;
import com.loopers.domain.product.ProductAggregateRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductAggregateRepositoryImpl implements ProductAggregateRepository {

	private final ProductAggregateJpaRepository productAggregateJpaRepository;

	@Override
	public Optional<ProductAggregate> findByProductId(final Long productId) {
		return productAggregateJpaRepository.findByProductId(productId);
	}

	@Override
	public Optional<ProductAggregate> findByProductIdWithOptimisticLock(final Long productId) {
		return productAggregateJpaRepository.findByIdWithOptimisticLock(productId);
	}

	@Override
	public Optional<ProductAggregate> findByProductIdWithPessimisticLock(final Long productId) {
		return productAggregateJpaRepository.findByIdWithPessimisticLock(productId);
	}

	@Override
	public Optional<ProductAggregate> findById(final Long id) {
		return productAggregateJpaRepository.findById(id);
	}

	@Override
	public ProductAggregate save(final ProductAggregate productAggregate) {
		return productAggregateJpaRepository.save(productAggregate);
	}

	@Override
	public boolean incrementLikeCount(final Long productId) {
		return productAggregateJpaRepository.incrementLikeCount(productId);
	}

	@Override
	public boolean decrementLikeCount(final Long productId) {
		return productAggregateJpaRepository.decrementLikeCount(productId);
	}

	@Override
	public boolean existsByProductId(final Long productId) {
		return productAggregateJpaRepository.existsByProductId(productId);
	}

}
