package com.loopers.infrastructure.payment;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.payment.PaymentHistory;
import com.loopers.domain.payment.PaymentHistoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {
	private final PaymentHistoryJpaRepository paymentHistoryJpaRepository;

	@Override
	public PaymentHistory save(final PaymentHistory paymentHistory) {
		return paymentHistoryJpaRepository.save(paymentHistory);
	}

	@Override
	public Optional<PaymentHistory> findById(final Long id) {
		return paymentHistoryJpaRepository.findById(id);
	}
}
