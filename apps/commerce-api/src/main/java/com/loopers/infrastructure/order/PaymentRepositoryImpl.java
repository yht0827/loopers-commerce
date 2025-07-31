package com.loopers.infrastructure.order;

import org.springframework.stereotype.Repository;

import com.loopers.domain.order.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentJpaRepository paymentJpaRepository;
}
