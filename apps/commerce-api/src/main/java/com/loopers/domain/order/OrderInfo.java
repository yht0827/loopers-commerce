package com.loopers.domain.order;

import java.util.List;

import com.loopers.domain.common.UserId;

public record OrderInfo(
	Long orderId,
	UserId userId,
	TotalOrderPrice totalPrice,
	OrderStatus status,
	List<OrderItem> items
) {
	public static OrderInfo from(Order order, List<OrderItem> items) {
		return new OrderInfo(
			order.getId(),
			order.getUserId(),
			order.getTotalOrderPrice(),
			order.getStatus(),
			items
		);
	}

}
