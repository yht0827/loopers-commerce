package com.loopers.infrastructure.order;

import org.springframework.stereotype.Repository;

import com.loopers.domain.order.OrderRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;
}
