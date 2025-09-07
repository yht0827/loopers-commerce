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
public class CouponName implements Serializable {
	
	@Column(name = "coupon_name")
	private String couponName;
	
	public CouponName(String couponName) {
		if (couponName == null || couponName.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 이름은 비어있을 수 없습니다.");
		}
		this.couponName = couponName;
	}
}
