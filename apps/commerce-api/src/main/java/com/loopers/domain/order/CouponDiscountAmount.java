package com.loopers.domain.order;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record CouponDiscountAmount(Long couponDiscountAmount) implements Serializable {
	public CouponDiscountAmount {
		if (couponDiscountAmount == null || couponDiscountAmount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 할인 금액은 0 이상이어야 합니다.");
		}
	}

	public static CouponDiscountAmount of(final Long amount) {
		return new CouponDiscountAmount(amount);
	}

}
