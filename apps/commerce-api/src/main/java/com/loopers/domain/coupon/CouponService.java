package com.loopers.domain.coupon;

import org.springframework.stereotype.Service;

import com.loopers.domain.order.CouponDiscountAmount;
import com.loopers.domain.order.TotalOrderPrice;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;

	public CouponDiscountAmount applyDiscounts(Long couponId, TotalOrderPrice totalOrderPrice) {
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 [id = " + couponId + "]의 쿠폰이 존재하지 않습니다."));

		return coupon.applyDiscount(totalOrderPrice);
	}
}
