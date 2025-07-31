package com.loopers.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.order.entity.OrderItem;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {
}
