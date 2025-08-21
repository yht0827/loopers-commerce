package com.loopers.domain.order;

import java.util.List;

public record OrderData() {

	public record CreateOrder(String userId, List<OrderItem> items, Long couponId) {
	}

	public record GetOrder(String userId, Long orderId) {
	}

	public record GetOrders(String userId) {
	}
}
