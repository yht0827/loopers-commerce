package com.loopers.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.order.entity.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
