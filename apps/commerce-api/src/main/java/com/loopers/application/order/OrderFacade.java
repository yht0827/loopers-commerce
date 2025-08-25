package com.loopers.application.order;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.order.OrderData;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;
	private final OrderProcessor orderProcessor;

	@Transactional
	public OrderResult createOrder(final OrderCommand.CreateOrder command) {
		OrderInfo orderInfo = orderProcessor.process(command);
		return OrderResult.from(orderInfo);
	}

	public List<OrderResult> getOrders(final OrderQuery.GetOrders command) {
		OrderData.GetOrders orderData = command.toData();
		List<OrderInfo> orderList = orderService.getOrders(orderData);
		return orderList.stream().map(OrderResult::from).toList();
	}

	public OrderResult getOrder(final OrderQuery.GetOrder command) {
		OrderData.GetOrder orderData = command.toData();
		OrderInfo orderInfo = orderService.getOrder(orderData);
		return OrderResult.from(orderInfo);
	}

}
