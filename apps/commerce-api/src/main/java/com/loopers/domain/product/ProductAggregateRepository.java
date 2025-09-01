package com.loopers.domain.product;

import java.util.Optional;

public interface ProductAggregateRepository {

	Optional<ProductAggregate> findByProductId(final Long productId);

	Optional<ProductAggregate> findByProductIdWithOptimisticLock(final Long productId);

	Optional<ProductAggregate> findByProductIdWithPessimisticLock(final Long productId);

	Optional<ProductAggregate> findById(final Long id);
	
	ProductAggregate save(final ProductAggregate productAggregate);
	
	// Update Query 메서드들 (도메인 친화적)
	boolean tryIncrementLikeCount(final Long productId);
	
	boolean tryDecrementLikeCount(final Long productId);

}
