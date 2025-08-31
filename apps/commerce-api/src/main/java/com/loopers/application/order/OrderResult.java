package com.loopers.application.order;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderStatus;

public record OrderResult(
	String orderId,
	String userId,
	Long totalPrice,
	OrderStatus status
) {

	public static OrderResult from(OrderInfo info) {
		return new OrderResult(info.orderId(), info.userId(), info.totalPrice(), info.status());
	}
}
