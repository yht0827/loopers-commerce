package com.loopers.application.order;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.order.TotalOrderPrice;

public record OrderResult(
	Long orderId,
	Long userId,
	TotalOrderPrice totalPrice,
	OrderStatus status
) {

	public static OrderResult from(OrderInfo info) {
		return new OrderResult(info.orderId(), info.userId(), info.totalPrice(), info.status());
	}
}
