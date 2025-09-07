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
public class CouponUsedAt implements Serializable {

	@Column(name = "used_at")
	private ZonedDateTime couponUsedAt;
	
	public CouponUsedAt(ZonedDateTime couponUsedAt) {
		if (couponUsedAt == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 사용일은 비어있을 수 없습니다.");
		}
		this.couponUsedAt = couponUsedAt;
	}

	public void update() {
		this.couponUsedAt = ZonedDateTime.now();
	}
}
