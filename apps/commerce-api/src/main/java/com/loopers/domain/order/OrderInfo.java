package com.loopers.domain.order;

import java.util.List;

public record OrderInfo(
	String orderId,
	String userId,
	Long totalPrice,
	OrderStatus status,
	List<OrderItem> items
) {
	public static OrderInfo from(Order order, List<OrderItem> items) {
		return new OrderInfo(
			order.getOrderNumber().orderNumber(),
			order.getUserId().userId(),
			order.getTotalOrderPrice().totalPrice(),
			order.getStatus(),
			items
		);
	}

}
