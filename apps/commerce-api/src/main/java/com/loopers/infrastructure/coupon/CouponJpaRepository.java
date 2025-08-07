package com.loopers.infrastructure.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.coupon.Coupon;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
}
