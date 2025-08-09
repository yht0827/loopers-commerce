package com.loopers.domain.coupon;

import java.time.ZonedDateTime;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record CouponIssuedAt(ZonedDateTime couponIssuedAt) {

	public CouponIssuedAt {
		if (couponIssuedAt == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 발행일은 비어있을 수 없습니다.");
		}

		if (couponIssuedAt.isAfter(ZonedDateTime.now())) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 발행일은 현재 시간보다 과거여야 합니다.");
		}
	}
}
