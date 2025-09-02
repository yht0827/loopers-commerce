package com.loopers.infrastructure.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.loopers.domain.product.ProductAggregate;

import jakarta.persistence.LockModeType;

public interface ProductAggregateJpaRepository extends JpaRepository<ProductAggregate, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT pa FROM ProductAggregate pa WHERE pa.productId.productId = :productId")
	Optional<ProductAggregate> findByIdWithPessimisticLock(final Long productId);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("SELECT pa FROM ProductAggregate pa WHERE pa.productId.productId = :productId")
	Optional<ProductAggregate> findByIdWithOptimisticLock(final Long productId);

	@Query("SELECT pa FROM ProductAggregate pa WHERE pa.productId.productId = :productId")
	Optional<ProductAggregate> findByProductId(final Long productId);

	@Modifying
	@Query("UPDATE ProductAggregate pa SET pa.likeCount.likeCount = pa.likeCount.likeCount + 1 WHERE pa.productId.productId = :productId")
	boolean incrementLikeCount(@Param("productId") Long productId);

	@Modifying
	@Query("UPDATE ProductAggregate pa SET pa.likeCount.likeCount = CASE WHEN pa.likeCount.likeCount > 0 THEN pa.likeCount.likeCount - 1 ELSE 0 END WHERE pa.productId.productId = :productId")
	boolean decrementLikeCount(@Param("productId") Long productId);

	@Query("SELECT COUNT(pa) > 0 FROM ProductAggregate pa WHERE pa.productId.productId = :productId")
	boolean existsByProductId(Long productId);
}
