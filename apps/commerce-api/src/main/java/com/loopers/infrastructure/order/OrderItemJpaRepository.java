package com.loopers.infrastructure.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.order.OrderItem;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

	@Query("SELECT oi FROM OrderItem oi WHERE oi.orderId.orderId = :orderId")
	List<OrderItem> findAllByOrderIdIn(final String orderId);

	@Query("SELECT oi FROM OrderItem oi WHERE oi.orderId.orderId IN :orderIds")
	List<OrderItem> findAllByOrderIdIns(final List<String> orderIds);
}
