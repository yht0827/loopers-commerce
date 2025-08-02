package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderResult;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.order.TotalOrderPrice;

public record OrderResponse(
	Long orderId,
	Long userId,
	TotalOrderPrice totalPrice,
	OrderStatus status
) {

	public static OrderResponse from(OrderResult orderResult) {
		return new OrderResponse(orderResult.orderId(), orderResult.userId(), orderResult.totalPrice(), orderResult.status());
	}
}
