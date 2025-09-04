package com.loopers.domain.product;

import java.util.Optional;

public interface ProductAggregateRepository {

	Optional<ProductAggregate> findByProductId(final Long productId);

	Optional<ProductAggregate> findByProductIdWithOptimisticLock(final Long productId);

	Optional<ProductAggregate> findByProductIdWithPessimisticLock(final Long productId);

	Optional<ProductAggregate> findById(final Long id);

	ProductAggregate save(final ProductAggregate productAggregate);

	boolean incrementLikeCount(final Long productId);

	boolean decrementLikeCount(final Long productId);

	boolean existsByProductId(final Long productId);
}
