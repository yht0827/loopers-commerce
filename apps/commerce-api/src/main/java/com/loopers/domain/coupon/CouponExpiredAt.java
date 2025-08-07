package com.loopers.domain.coupon;

import java.time.ZonedDateTime;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record CouponExpiredAt(ZonedDateTime couponExpiredAt) {

	public CouponExpiredAt {
		if (couponExpiredAt == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 만료일은 비어있을 수 없습니다.");
		}

		if (couponExpiredAt.isBefore(ZonedDateTime.now())) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 만료일은 현재 시간보다 미래여야 합니다.");
		}
	}
}
