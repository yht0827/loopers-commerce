package com.loopers.infrastructure.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.order.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

	@Query("SELECT o FROM Order o WHERE o.userId.userId = :userId")
	List<Order> findAllOrdersByUserId(final String userId);

	@Query("SELECT o FROM Order o WHERE o.userId.userId = :userId AND o.id = :orderId")
	Optional<Order> findByIdAndUserId(Long orderId, String userId);
}
