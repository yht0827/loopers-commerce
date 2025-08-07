package com.loopers.domain.coupon;

import com.loopers.support.util.EnumMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatus {

	ACTIVE, INACTIVE, EXPIRED, USED;

	private static final EnumMapper<CouponStatus> MAPPER = new EnumMapper<>(CouponStatus.class);

	public static CouponStatus from(String type) {
		return MAPPER.from(type, "유효하지 않은 쿠폰 상태 입니다.");
	}
}
