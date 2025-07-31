package com.loopers.domain.order.entity.vo;

import java.math.BigDecimal;

import com.loopers.domain.order.entity.OrderItem;

public record OrderItemVO(Long id, Long orderId, Long skuId, String orderNumber, Long quantity, BigDecimal price) {
	public static OrderItemVO from(OrderItem orderItem) {
		return new OrderItemVO(
			orderItem.getId(),
			orderItem.getIdentifier().orderId().orderId(),
			orderItem.getIdentifier().skuId(),
			orderItem.getIdentifier().orderNumber(),
			orderItem.getDetail().quantity(),
			orderItem.getDetail().price()
		);
	}
}
