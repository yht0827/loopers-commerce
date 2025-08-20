package com.loopers.application.order;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.order.DiscountInfo;
import com.loopers.domain.order.OrderData;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;

	@Transactional
	public OrderResult createOrder(final OrderCommand.CreateOrder command) {
		OrderData.CreateOrder orderData = command.toData();

		// 1. 요청된 상품 정보로 OrderItem 리스트 생성 및 재고 차감
		List<OrderItem> orderItems = orderService.createOrderItems(orderData.items());

		// 2. 할인 전 총 주문 금액 계산
		Long totalOrderPrice = orderService.calculateTotalOrderPrice(orderItems);

		// 3. 쿠폰 및 포인트 할인 적용
		DiscountInfo discountInfo = orderService.applyDiscounts(orderData, totalOrderPrice);

		// 4. 주문 저장
		OrderInfo orderInfo = orderService.saveOrder(orderData, orderItems, totalOrderPrice, discountInfo);

		return OrderResult.from(orderInfo);
	}

	public List<OrderResult> getOrders(final OrderCommand.GetOrders command) {
		OrderData.GetOrders orderData = command.toData();
		List<OrderInfo> orderList = orderService.getOrders(orderData);
		return orderList.stream().map(OrderResult::from).toList();
	}

	public OrderResult getOrder(final OrderCommand.GetOrder command) {
		OrderData.GetOrder orderData = command.toData();
		OrderInfo orderInfo = orderService.getOrder(orderData);
		return OrderResult.from(orderInfo);
	}

}
