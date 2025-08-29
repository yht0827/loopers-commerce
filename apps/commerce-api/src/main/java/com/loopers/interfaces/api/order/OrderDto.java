package com.loopers.interfaces.api.order;

import java.util.List;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.OrderQuery;
import com.loopers.application.order.OrderResult;
import com.loopers.domain.order.OrderStatus;

public record OrderDto() {

	public record V1() {

		public record OrderRequest(List<OrderItemRequest> items, Long couponId, String cardType, String cardNo,
								   String callbackUrl) {
			public OrderCommand.CreateOrder toCommand(final String userId) {
				List<OrderCommand.OrderItemCommand> items = this.items.stream()
					.map(OrderItemRequest::toCommand)
					.toList();

				return new OrderCommand.CreateOrder(userId, items, couponId, cardType, cardNo, callbackUrl);
			}
		}

		public record OrderItemRequest(Long productId, Long quantity, Long price) {
			public OrderCommand.OrderItemCommand toCommand() {
				return new OrderCommand.OrderItemCommand(productId, quantity, price);
			}
		}

		public record getOrdersRequest(String userId) {
			public static OrderQuery.GetOrders toCommand(final String userId) {
				return new OrderQuery.GetOrders(userId);
			}
		}

		public record getOrderRequest(String userId, Long orderId) {
			public static OrderQuery.GetOrder toCommand(final String userId, final Long orderId) {
				return new OrderQuery.GetOrder(userId, orderId);
			}
		}

		public record OrderResponse(String orderId, String userId, Long totalPrice, OrderStatus status) {
			public static OrderResponse from(OrderResult orderResult) {
				return new OrderResponse(orderResult.orderId(), orderResult.userId(), orderResult.totalPrice(),
					orderResult.status());
			}
		}

	}
}
