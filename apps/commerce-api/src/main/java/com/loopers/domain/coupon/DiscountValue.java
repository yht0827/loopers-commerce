package com.loopers.domain.coupon;

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
public class DiscountValue implements Serializable {

	@Column(name = "discount_value")
	private Long discountValue;

	public DiscountValue(Long discountValue) {
		if (discountValue == null || discountValue < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "할인 금액은 0 이상이어야 합니다.");
		}
		this.discountValue = discountValue;
	}

	public Long calculateFixedDiscount(Long amount) {
		return Math.min(discountValue, amount);
	}

	public Long calculatePercentageDiscount(Long amount) {
		return (long)(amount * (discountValue / 100.0));
	}

}
