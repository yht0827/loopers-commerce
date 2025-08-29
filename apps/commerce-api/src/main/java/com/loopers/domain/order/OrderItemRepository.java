package com.loopers.domain.order;

import java.util.List;

public interface OrderItemRepository {

	OrderItem save(final OrderItem orderItem);

	List<OrderItem> saveAll(List<OrderItem> orderItem);

	List<OrderItem> findAllByOrderId(final String orderId);

	List<OrderItem> findAllByOrderIdIn(final List<String> orderIds);
}
