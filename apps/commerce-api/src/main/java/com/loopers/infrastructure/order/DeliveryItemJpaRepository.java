package com.loopers.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.order.entity.DeliveryItem;

public interface DeliveryItemJpaRepository extends JpaRepository<DeliveryItem, Long> {
}
