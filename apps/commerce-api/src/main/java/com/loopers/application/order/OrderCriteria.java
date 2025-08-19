package com.loopers.application.order;

import java.util.List;

import com.loopers.domain.order.OrderCommand;

public record OrderCriteria() {

	public record CreateOrder(String userId, List<OrderItem> items, Long couponId) {
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

	public record GetOrders(String userId) {

		public OrderCommand.GetOrders toCommand() {
			return new OrderCommand.GetOrders(userId);
		}
	}

	public record GetOrder(String userId, Long orderId) {

		public OrderCommand.GetOrder toCommand() {
			return new OrderCommand.GetOrder(userId, orderId);
		}
	}

}
