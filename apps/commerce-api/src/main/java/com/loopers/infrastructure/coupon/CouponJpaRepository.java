package com.loopers.infrastructure.coupon;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.coupon.Coupon;

import jakarta.persistence.LockModeType;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Coupon c WHERE c.id = :id")
	Optional<Coupon> findByIdWithPessimisticLock(Long id);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("SELECT c FROM Coupon c WHERE c.id = :id")
	Optional<Coupon> findByIdWithOptimisticLock(Long id);
}
