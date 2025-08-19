package com.loopers.infrastructure.payment;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
	private final PaymentJpaRepository paymentJpaRepository;

	@Override
	public Payment save(final Payment payment) {
		return paymentJpaRepository.save(payment);
	}

	@Override
	public Optional<Payment> findById(final Long id) {
		return paymentJpaRepository.findById(id);
	}
}
