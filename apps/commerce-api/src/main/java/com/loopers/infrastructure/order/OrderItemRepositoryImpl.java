package com.loopers.infrastructure.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

	private final OrderItemJpaRepository orderItemJpaRepository;

	@Override
	public OrderItem save(final OrderItem orderItem) {
		return orderItemJpaRepository.save(orderItem);
	}

	@Override
	public List<OrderItem> saveAll(final List<OrderItem> orderItem) {
		return orderItemJpaRepository.saveAll(orderItem);
	}

	@Override
	public List<OrderItem> findAllByOrderId(final Long orderId) {
		return orderItemJpaRepository.findAllByOrderId(orderId);
	}

	@Override
	public List<OrderItem> findAllByOrderIdIn(final List<Long> orderIds) {
		return orderItemJpaRepository.findAllByOrderIdIn(orderIds);
	}
}
