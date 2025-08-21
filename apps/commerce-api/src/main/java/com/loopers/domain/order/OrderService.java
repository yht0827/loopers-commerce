package com.loopers.domain.order;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;

	public TotalOrderPrice calculateTotalOrderPrice(List<OrderItem> orderItems) {
		return TotalOrderPrice.of(orderItems);
	}

	public OrderInfo saveOrder(OrderData.CreateOrder data, List<OrderItem> orderItems, TotalOrderPrice totalOrderPrice,
		CouponDiscountAmount couponDiscountAmount) {

		Order order = Order.create(data, totalOrderPrice, couponDiscountAmount);
		Order savedOrder = orderRepository.save(order);
		List<OrderItem> orderItemList = orderItemRepository.saveAll(orderItems);

		return OrderInfo.from(savedOrder, orderItemList);
	}

	@Transactional(readOnly = true)
	public List<OrderInfo> getOrders(final OrderData.GetOrders data) {
		List<Order> orderList = orderRepository.findAllOrdersByUserId(data.userId());

		if (orderList.isEmpty()) {
			return Collections.emptyList();
		}

		List<Long> orderIds = orderList.stream().map(Order::getId).collect(Collectors.toList());

		List<OrderItem> allOrderItems = orderItemRepository.findAllByOrderIdIn(orderIds);

		Map<Long, List<OrderItem>> orderItemMap = allOrderItems.stream().collect(Collectors.groupingBy(BaseEntity::getId));

		return orderList.stream().map(order -> {
			List<OrderItem> orderItems = orderItemMap.getOrDefault(order.getId(), Collections.emptyList());
			return OrderInfo.from(order, orderItems);
		}).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public OrderInfo getOrder(final OrderData.GetOrder data) {
		Order order = orderRepository.findByIdAndUserId(data.orderId(), data.userId())
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "주문을 찾을 수 없습니다."));

		List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(data.orderId());

		return OrderInfo.from(order, orderItems);
	}
}
