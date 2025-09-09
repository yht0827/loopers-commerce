package com.loopers.domain.coupon;

import java.io.Serializable;
import java.time.ZonedDateTime;

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
public class CouponIssuedAt implements Serializable {

	@Column(name = "issued_at")
	private ZonedDateTime couponIssuedAt;
	
	public CouponIssuedAt(ZonedDateTime couponIssuedAt) {
		if (couponIssuedAt == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 발행일은 비어있을 수 없습니다.");
		}

		if (couponIssuedAt.isAfter(ZonedDateTime.now())) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 발행일은 현재 시간보다 과거여야 합니다.");
		}
		this.couponIssuedAt = couponIssuedAt;
	}
}
