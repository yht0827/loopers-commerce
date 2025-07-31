package com.loopers.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.order.entity.OrderStatusHistory;

public interface OrderStatusHistoryJpaRepository extends JpaRepository<OrderStatusHistory, Long> {
}
