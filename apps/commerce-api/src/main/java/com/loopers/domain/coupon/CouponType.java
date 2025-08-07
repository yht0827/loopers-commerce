package com.loopers.domain.coupon;

import com.loopers.support.util.EnumMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType {
	PRODUCT, BRAND;

	private static final EnumMapper<CouponType> MAPPER = new EnumMapper<>(CouponType.class);

	public static CouponType from(String type) {
		return MAPPER.from(type, "유효하지 않은 쿠폰 타입 입니다.");
	}
}
