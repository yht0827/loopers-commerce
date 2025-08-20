package com.loopers.interfaces.api.order;

import java.util.List;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.OrderResult;
import com.loopers.domain.common.OrderId;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.order.TotalOrderPrice;

public record OrderDto() {

	public record V1() {

		public record OrderRequest(List<OrderItemRequest> items, Long couponId) {
			public OrderCommand.CreateOrder toCommand(final String userId) {
				final List<OrderCommand.CreateOrder.OrderItem> items = this.items.stream()
					.map(OrderItemRequest::toCommand)
					.toList();

				return new OrderCommand.CreateOrder(userId, items, couponId);
			}
		}

		public record getOrdersRequest(String userId) {
			public static OrderCommand.GetOrders toCommand(final String userId) {
				return new OrderCommand.GetOrders(userId);
			}
		}

		public record getOrderRequest(String userId, OrderId orderId) {
			public static OrderCommand.GetOrder toCommand(final String userId, final Long orderId) {
				return new OrderCommand.GetOrder(userId, orderId);
			}
		}

		public record OrderItemRequest(Long productId, Long quantity) {
			public OrderCommand.CreateOrder.OrderItem toCommand() {
				return new OrderCommand.CreateOrder.OrderItem(productId, quantity);
			}
		}

		public record OrderResponse(Long orderId, String userId, TotalOrderPrice totalPrice, OrderStatus status) {
			public static OrderResponse from(OrderResult orderResult) {
				return new OrderResponse(orderResult.orderId(), orderResult.userId(), orderResult.totalPrice(),
					orderResult.status());
			}
		}

	}
}
