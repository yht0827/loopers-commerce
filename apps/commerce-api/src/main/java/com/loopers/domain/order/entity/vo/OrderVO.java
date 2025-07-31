package com.loopers.domain.order.entity.vo;

import java.math.BigDecimal;

import com.loopers.domain.order.entity.Order;

public record OrderVO(Long id, Long userId, String orderNumber, BigDecimal totalPrice, String status) {
	public static OrderVO from(Order order) {
		return new OrderVO(
			order.getId(),
			order.getUserId(),
			order.getOrderNumber().orderNumber(),
			order.getTotalPrice().totalPrice(),
			order.getStatus().name()
		);
	}
}
