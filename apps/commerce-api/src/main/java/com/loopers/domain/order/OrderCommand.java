package com.loopers.domain.order;

import java.util.List;

public record OrderCommand() {

	public record CreateOrder(String userId, List<OrderItem> items, Long couponId) {

		public record OrderItem(Long productId, Long quantity) {
		}
	}

	public record GetOrder(String userId, Long orderId) {
	}

	public record GetOrders(String userId) {
	}
}
