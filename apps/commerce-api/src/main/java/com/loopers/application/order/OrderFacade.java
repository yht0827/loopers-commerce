package com.loopers.application.order;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.interfaces.api.order.OrderItemRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;

	public OrderResult createOrder(Long userId, List<OrderItemRequest> itemRequests) {
		OrderInfo order = orderService.createOrder(userId, itemRequests);

		return OrderResult.from(order);
	}

	public List<OrderResult> getOrdersById(Long userId) {
		List<OrderInfo> orderList = orderService.getOrdersById(userId);
		return orderList.stream().map(OrderResult::from).toList();
	}

	public OrderResult getOrderDetails(Long userId, Long orderId) {
		OrderInfo orderInfo = orderService.getOrderDetails(userId, orderId);
		return OrderResult.from(orderInfo);
	}

}
