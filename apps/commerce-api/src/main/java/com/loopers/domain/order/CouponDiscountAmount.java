package com.loopers.domain.order;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponDiscountAmount implements Serializable {

	@Column(name = "coupon_discount_amount")
	private Long couponDiscountAmount;

	public CouponDiscountAmount(Long couponDiscountAmount) {
		if (couponDiscountAmount == null || couponDiscountAmount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 할인 금액은 0 이상이어야 합니다.");
		}
		this.couponDiscountAmount = couponDiscountAmount;
	}

	public static CouponDiscountAmount of(final Long amount) {
		return new CouponDiscountAmount(amount);
	}
}
