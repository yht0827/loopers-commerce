package com.loopers.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

	List<Order> findAllOrdersByUserId(String userId);

	Optional<Order> findByIdAndUserId(Long orderId, String userId);

	Order save(Order order);

	Optional<Order> findByOrderNumber(String orderId);
}
