package com.loopers.application.order;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;

	public OrderResult createOrder(final OrderCriteria.CreateOrder criteria) {
		OrderCommand.CreateOrder command = criteria.toCommand();
		OrderInfo order = orderService.createOrder(command);

		return OrderResult.from(order);
	}

	public List<OrderResult> getOrders(final OrderCriteria.GetOrders criteria) {
		OrderCommand.GetOrders command = criteria.toCommand();
		List<OrderInfo> orderList = orderService.getOrders(command);
		return orderList.stream().map(OrderResult::from).toList();
	}

	public OrderResult getOrder(final OrderCriteria.GetOrder criteria) {
		OrderCommand.GetOrder command = criteria.toCommand();

		OrderInfo orderInfo = orderService.getOrder(command);
		return OrderResult.from(orderInfo);
	}

}
