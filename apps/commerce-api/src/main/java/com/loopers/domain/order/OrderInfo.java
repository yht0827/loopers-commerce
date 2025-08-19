package com.loopers.domain.order;

import java.util.List;

public record OrderInfo(
	Long orderId,
	String userId,
	TotalOrderPrice totalPrice,
	OrderStatus status,
	List<OrderItem> items
) {
	public static OrderInfo from(Order order, List<OrderItem> items) {
		return new OrderInfo(
			order.getId(),
			order.getUserId().userId(),
			order.getTotalOrderPrice(),
			order.getStatus(),
			items
		);
	}

}
