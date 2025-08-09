package com.loopers.infrastructure.like;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.like.Like;

import jakarta.persistence.LockModeType;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {

	@Query("SELECT l FROM Like l WHERE l.targetId.userId = :userId")
	List<Like> findAllByUserId(Long userId);

	@Query("SELECT l FROM Like l WHERE l.targetId.userId = :userId AND l.targetId.targetId = :productId")
	Optional<Like> findByUserIdAndProductId(Long userId, Long productId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT l FROM Like l WHERE l.targetId.userId = :userId AND l.targetId.targetId = :productId")
	Optional<Like> findByUserIdAndProductIdWithPessimisticLock(Long userId, Long productId);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("SELECT l FROM Like l WHERE l.targetId.userId = :userId AND l.targetId.targetId = :productId")
	Optional<Like> findByUserIdAndProductIdWithOptimisticLock(Long userId, Long productId);
}
