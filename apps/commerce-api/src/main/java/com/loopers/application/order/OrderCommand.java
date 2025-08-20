package com.loopers.application.order;

import java.util.List;

import com.loopers.domain.order.OrderData;

public record OrderCommand() {

	public record CreateOrder(String userId, List<OrderItem> items, Long couponId) {
		public OrderData.CreateOrder toData() {
			final List<OrderData.CreateOrder.OrderItem> items = this.items.stream()
				.map(orderItem -> new OrderData.CreateOrder.OrderItem(
					orderItem.productId, orderItem.quantity))
				.toList();

			return new OrderData.CreateOrder(userId, items, couponId);
		}

		public record OrderItem(Long productId, Long quantity) {
		}
	}

	public record GetOrders(String userId) {

		public OrderData.GetOrders toData() {
			return new OrderData.GetOrders(userId);
		}
	}

	public record GetOrder(String userId, Long orderId) {

		public OrderData.GetOrder toData() {
			return new OrderData.GetOrder(userId, orderId);
		}
	}

}
