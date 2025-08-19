package com.loopers.infrastructure.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.payment.PaymentHistory;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistory, Long> {
}
