package com.loopers.infrastructure.order;

import org.springframework.stereotype.Repository;

import com.loopers.domain.order.DeliveryItemRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryItemRepositoryImpl implements DeliveryItemRepository {

	private final DeliveryItemJpaRepository deliveryItemJpaRepository;
}
