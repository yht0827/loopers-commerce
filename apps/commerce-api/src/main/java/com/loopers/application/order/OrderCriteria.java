package com.loopers.application.order;

import java.util.List;

import com.loopers.domain.order.OrderCommand;

public record OrderCriteria() {

	public record CreateOrder(Long userId, List<OrderItem> items, Long couponId) {
		public OrderCommand.CreateOrder toCommand() {
			final List<OrderCommand.CreateOrder.OrderItem> items = this.items.stream()
				.map(orderItem -> new OrderCommand.CreateOrder.OrderItem(
					orderItem.productId, orderItem.quantity))
				.toList();

			return new OrderCommand.CreateOrder(userId, items, couponId);
		}

		public record OrderItem(Long productId, Long quantity) {
		}
	}

}
