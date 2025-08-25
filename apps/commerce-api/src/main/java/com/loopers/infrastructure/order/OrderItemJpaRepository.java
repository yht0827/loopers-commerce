package com.loopers.infrastructure.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.order.OrderItem;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

	@Query("SELECT oi FROM OrderItem oi WHERE oi.orderId.orderId = :orderId")
	List<OrderItem> findAllByOrderId(final Long orderId);

	@Query("SELECT oi FROM OrderItem oi WHERE oi.orderId.orderId IN :orderIds")
	List<OrderItem> findAllByOrderIdIn(final List<Long> orderIds);
}
