package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.BrandId;
import com.loopers.domain.common.ProductId;
import com.loopers.domain.common.UserId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

	private UserId userId;
	private ProductId productId;
	private BrandId brandId;
	private CouponName couponName;
	private DiscountValue discountValue;
	private MaxDisCountAmount maxDiscountAmount;
	private CouponType couponType;
	private CouponIssuedAt couponIssuedAt;
	private CouponUsedAt couponUsedAt;
	private CouponExpiredAt couponExpiredAt;
	private CouponStatus couponStatus;

	@Version
	private Long version;

	@Builder
	public Coupon(UserId userId, ProductId productId, BrandId brandId, CouponName couponName, DiscountValue discountValue,
		MaxDisCountAmount maxDiscountAmount, CouponType couponType, CouponIssuedAt couponIssuedAt, CouponUsedAt couponUsedAt,
		CouponExpiredAt couponExpiredAt, CouponStatus couponStatus) {
		this.userId = userId;
		this.productId = productId;
		this.brandId = brandId;
		this.couponName = couponName;
		this.discountValue = discountValue;
		this.maxDiscountAmount = maxDiscountAmount;
		this.couponType = couponType;
		this.couponIssuedAt = couponIssuedAt;
		this.couponUsedAt = couponUsedAt;
		this.couponExpiredAt = couponExpiredAt;
		this.couponStatus = couponStatus;
	}

	public void use() {
		if (couponStatus == CouponStatus.USED) {
			throw new CoreException(ErrorType.BAD_REQUEST, "이미 사용된 쿠폰입니다.");
		}

		this.couponUsedAt.update();
		this.couponStatus = CouponStatus.USED;
	}

	public Long calculateDisCount(final Long amount) {
		if (couponType == CouponType.FIXED_AMOUNT) {
			return Math.min(this.discountValue.discountValue(), amount);
		}

		// 정률 할인
		long calculatedDiscount = (long)(amount * (this.discountValue.discountValue() / 100.0));

		// 최대 할인 금액이 설정된 경우, 이를 적용합니다.
		if (maxDiscountAmount != null && maxDiscountAmount.maxDisCountAmount() != null) {
			return Math.min(calculatedDiscount, maxDiscountAmount.maxDisCountAmount());
		}

		return calculatedDiscount;

	}

}
