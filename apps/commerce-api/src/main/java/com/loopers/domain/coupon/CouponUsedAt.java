package com.loopers.domain.coupon;

import java.time.ZonedDateTime;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record CouponUsedAt(ZonedDateTime couponUsedAt) {

	public CouponUsedAt {
		if (couponUsedAt == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 사용일은 비어있을 수 없습니다.");
		}
	}

	public void update() {
		new CouponUsedAt(ZonedDateTime.now());
	}

}
