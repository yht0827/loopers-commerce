package com.loopers.infrastructure.order;

import org.springframework.stereotype.Repository;

import com.loopers.domain.order.OrderStatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderStatusHistoryRepositoryImpl implements OrderStatusHistoryRepository {

	private final OrderStatusHistoryJpaRepository orderStatusHistoryJpaRepository;
}
