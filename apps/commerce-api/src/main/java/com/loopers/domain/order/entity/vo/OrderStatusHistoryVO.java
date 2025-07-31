package com.loopers.domain.order.entity.vo;

import com.loopers.domain.order.entity.OrderStatusHistory;

public record OrderStatusHistoryVO(Long id, Long orderId, String status) {
	public static OrderStatusHistoryVO from(OrderStatusHistory history) {
		return new OrderStatusHistoryVO(
			history.getId(),
			history.getOrderId().orderId(),
			history.getStatus().name()
		);
	}
}
