package com.loopers.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.order.entity.Delivery;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {
}
