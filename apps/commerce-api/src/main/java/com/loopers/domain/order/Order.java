package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.UserId;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

	private UserId userId;
	private TotalOrderPrice totalOrderPrice;
	private CouponDiscountAmount couponDiscountAmount;
	private OrderNumber orderNumber;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Builder
	public Order(UserId userId, TotalOrderPrice totalOrderPrice, CouponDiscountAmount couponDiscountAmount,
		OrderNumber orderNumber, OrderStatus status) {
		this.userId = userId;
		this.totalOrderPrice = totalOrderPrice;
		this.couponDiscountAmount = couponDiscountAmount;
		this.orderNumber = orderNumber;
		this.status = status;
	}
}
