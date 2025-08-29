package com.loopers.infrastructure.order;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public List<Order> findAllOrdersByUserId(String userId) {
		return orderJpaRepository.findAllOrdersByUserId(userId);
	}

	@Override
	public Optional<Order> findByIdAndUserId(final Long orderId, final String userId) {
		return orderJpaRepository.findByIdAndUserId(orderId, userId);
	}

	@Override
	public Order save(Order order) {
		return orderJpaRepository.save(order);
	}
}
