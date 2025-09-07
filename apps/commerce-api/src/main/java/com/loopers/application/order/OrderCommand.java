package com.loopers.application.order;

import java.util.List;

import com.loopers.domain.common.Price;
import com.loopers.domain.product.ProductId;
import com.loopers.domain.common.Quantity;
import com.loopers.domain.order.OrderData;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.payment.CardType;

public record OrderCommand() {

	public record CreateOrder(String userId, List<OrderItemCommand> items, Long couponId, CardType cardType, String cardNo,
							  String callbackUrl) {
		public OrderData.CreateOrder toData() {
			List<OrderItem> items = this.items.stream()
				.map(OrderItemCommand::toData)
				.toList();

			return new OrderData.CreateOrder(userId, items, couponId);
		}
	}

	public record OrderItemCommand(Long productId, Long quantity, Long price) {
		public OrderItem toData() {
			return OrderItem.builder()
				.productId(new ProductId(productId))
				.quantity(new Quantity(quantity))
				.price(new Price(price))
				.build();
		}
	}

}
