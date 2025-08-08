package com.loopers.application.order;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.order.DiscountInfo;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;

	@Transactional
	public OrderResult createOrder(final OrderCriteria.CreateOrder criteria) {
		OrderCommand.CreateOrder command = criteria.toCommand();

		// 1. 요청된 상품 정보로 OrderItem 리스트 생성 및 재고 차감
		List<OrderItem> orderItems = orderService.createOrderItems(command.items());

		// 2. 할인 전 총 주문 금액 계산
		Long totalOrderPrice = orderService.calculateTotalOrderPrice(orderItems);

		// 3. 쿠폰 및 포인트 할인 적용
		DiscountInfo discountInfo = orderService.applyDiscounts(command, totalOrderPrice);

		// 4. 주문 저장
		OrderInfo orderInfo = orderService.saveOrder(command, orderItems, totalOrderPrice, discountInfo);

		return OrderResult.from(orderInfo);
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
