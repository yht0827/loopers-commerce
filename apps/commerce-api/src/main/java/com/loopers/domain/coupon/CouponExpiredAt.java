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
public class CouponExpiredAt implements Serializable {

	@Column(name = "expired_at")
	private ZonedDateTime couponExpiredAt;
	
	public CouponExpiredAt(ZonedDateTime couponExpiredAt) {
		if (couponExpiredAt == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 만료일은 비어있을 수 없습니다.");
		}

		if (couponExpiredAt.isBefore(ZonedDateTime.now())) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 만료일은 현재 시간보다 미래여야 합니다.");
		}
		this.couponExpiredAt = couponExpiredAt;
	}
}
