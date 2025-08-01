package com.loopers.domain.order;

public record OrderInfo(
	Long orderId,
	Long userId,
	TotalOrderPrice totalPrice,
	OrderStatus status
) {

	public static OrderInfo from(Order order) {
		return new OrderInfo(
			order.getId(),
			order.getUserId(),
			order.getTotalOrderPrice(),
			order.getStatus()
		);
	}

}
