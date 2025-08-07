package com.loopers.infrastructure.coupon;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {

	private final CouponJpaRepository couponJpaRepository;

	@Override
	public Optional<Coupon> findById(final Long id) {
		return couponJpaRepository.findById(id);
	}
}
