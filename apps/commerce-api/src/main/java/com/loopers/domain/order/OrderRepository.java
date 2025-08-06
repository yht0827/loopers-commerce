package com.loopers.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

	List<Order> findAllOrdersByUserId(Long userId);

	Optional<Order> findByIdAndUserId(Long orderId, Long userId);

	Order save(Order order);
}
