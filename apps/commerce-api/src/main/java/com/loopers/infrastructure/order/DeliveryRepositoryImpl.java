package com.loopers.infrastructure.order;

import org.springframework.stereotype.Repository;

import com.loopers.domain.order.DeliveryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {
	private final DeliveryJpaRepository deliveryJpaRepository;
}
