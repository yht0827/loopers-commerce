package com.loopers.domain.coupon;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record DiscountPolicy(
	DiscountValue discountValue, MaxDisCountAmount maxDiscountAmount, CouponType couponType
) implements Serializable {

	public DiscountPolicy {
		if (discountValue == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "할인 값이 설정되지 않았습니다.");
		}
		if (couponType == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 타입이 설정되지 않았습니다.");
		}
	}

	public Long calculate(Long amount) {
		if (amount == null || amount < 0) {
			throw new IllegalArgumentException("할인 계산 대상 금액은 0 이상이어야 합니다.");
		}

		Long discount = (couponType == CouponType.FIXED_AMOUNT)
			? discountValue.calculateFixedDiscount(amount)
			: discountValue.calculatePercentageDiscount(amount);

		return (maxDiscountAmount != null && maxDiscountAmount.hasLimit())
			? maxDiscountAmount.applyMaxDiscountLimit(discount)
			: discount;
	}
}
