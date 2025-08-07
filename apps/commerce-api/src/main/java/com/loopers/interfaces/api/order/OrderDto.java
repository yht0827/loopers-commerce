package com.loopers.interfaces.api.order;

import java.util.List;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderResult;
import com.loopers.domain.common.OrderId;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.order.TotalOrderPrice;

public record OrderDto() {

	public record V1() {

		public record OrderRequest(List<OrderItemRequest> items, Long couponId) {
			public OrderCriteria.CreateOrder toCriteria(final Long userId) {
				final List<OrderCriteria.CreateOrder.OrderItem> items = this.items.stream()
					.map(OrderItemRequest::toCriteria)
					.toList();

				return new OrderCriteria.CreateOrder(userId, items, couponId);
			}
		}

		public record getOrdersRequest(Long userId) {
			public static OrderCriteria.GetOrders toCriteria(final Long userId) {
				return new OrderCriteria.GetOrders(userId);
			}
		}

		public record getOrderRequest(Long userId, OrderId orderId) {
			public static OrderCriteria.GetOrder toCriteria(final Long userId, final Long orderId) {
				return new OrderCriteria.GetOrder(userId, orderId);
			}
		}

		public record OrderItemRequest(Long productId, Long quantity) {
			public OrderCriteria.CreateOrder.OrderItem toCriteria() {
				return new OrderCriteria.CreateOrder.OrderItem(productId, quantity);
			}
		}

		public record OrderResponse(Long orderId, Long userId, TotalOrderPrice totalPrice, OrderStatus status) {
			public static OrderResponse from(OrderResult orderResult) {
				return new OrderResponse(orderResult.orderId(), orderResult.userId(), orderResult.totalPrice(),
					orderResult.status());
			}
		}

	}
}
