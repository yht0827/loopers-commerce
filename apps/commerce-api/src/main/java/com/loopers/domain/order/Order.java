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
	private FinalPaymentAmount finalPaymentAmount;
	private OrderNumber orderNumber;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Builder
	public Order(UserId userId, TotalOrderPrice totalOrderPrice, CouponDiscountAmount couponDiscountAmount,
		FinalPaymentAmount finalPaymentAmount, OrderNumber orderNumber, OrderStatus status) {
		this.userId = userId;
		this.totalOrderPrice = totalOrderPrice;
		this.couponDiscountAmount = couponDiscountAmount;
		this.finalPaymentAmount = finalPaymentAmount;
		this.orderNumber = orderNumber;
		this.status = status;
	}

	public static Order create(OrderData.CreateOrder data, TotalOrderPrice totalOrderPrice,
		CouponDiscountAmount couponDiscountAmount) {

		Long totalPrice = totalOrderPrice.totalPrice();
		Long discountAmount = couponDiscountAmount.couponDiscountAmount();
		FinalPaymentAmount finalPaymentAmount = FinalPaymentAmount.of(totalPrice, discountAmount);

		return Order.builder()
			.userId(new UserId(data.userId()))
			.totalOrderPrice(totalOrderPrice)
			.couponDiscountAmount(couponDiscountAmount)
			.finalPaymentAmount(finalPaymentAmount)
			.status(OrderStatus.PENDING)
			.build();
	}
}
