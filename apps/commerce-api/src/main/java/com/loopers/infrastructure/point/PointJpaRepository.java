package com.loopers.infrastructure.point;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.point.Point;

import jakarta.persistence.LockModeType;

public interface PointJpaRepository extends JpaRepository<Point, Long> {

	@Query("SELECT p FROM Point p WHERE p.userId = :userId")
	Optional<Point> findByUserId(Long userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Point p WHERE p.userId = :userId")
	Optional<Point> findByUserIdWithPessimisticLock(Long userId);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("SELECT p FROM Point p WHERE p.userId = :userId")
	Optional<Point> findByUserIdWithOptimisticLock(Long userId);

}
