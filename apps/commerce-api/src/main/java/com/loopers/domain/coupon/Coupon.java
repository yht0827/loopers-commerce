package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.BrandId;
import com.loopers.domain.common.ProductId;
import com.loopers.domain.common.UserId;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
	private CouponType couponType;
	private CouponIssuedAt couponIssuedAt;
	private CouponUsedAt couponUsedAt;
	private CouponExpiredAt couponExpiredAt;
	private CouponStatus couponStatus;

	@Builder
	public Coupon(UserId userId, ProductId productId, BrandId brandId, CouponName couponName, DiscountValue discountValue,
		CouponType couponType, CouponIssuedAt couponIssuedAt, CouponUsedAt couponUsedAt, CouponExpiredAt couponExpiredAt,
		CouponStatus couponStatus) {
		this.userId = userId;
		this.productId = productId;
		this.brandId = brandId;
		this.couponName = couponName;
		this.discountValue = discountValue;
		this.couponType = couponType;
		this.couponIssuedAt = couponIssuedAt;
		this.couponUsedAt = couponUsedAt;
		this.couponExpiredAt = couponExpiredAt;
		this.couponStatus = couponStatus;
	}

	public void use() {
		this.couponUsedAt.update();
		this.couponStatus = CouponStatus.USED;
	}

}
