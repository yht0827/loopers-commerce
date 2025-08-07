package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.OrderId;
import com.loopers.domain.common.Price;
import com.loopers.domain.common.ProductId;
import com.loopers.domain.common.Quantity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

	private OrderId orderId;

	private ProductId productId;

	private Quantity quantity;

	private Price price;

	@Builder
	public OrderItem(OrderId orderId, ProductId productId, Quantity quantity, Price price) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}
}
