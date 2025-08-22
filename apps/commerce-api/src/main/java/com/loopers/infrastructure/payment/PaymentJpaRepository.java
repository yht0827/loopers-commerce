package com.loopers.infrastructure.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.payment.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

	@Query("SELECT p FROM Payment p WHERE p.orderId.orderId = :orderId AND p.userId.userId = :userId")
	Optional<Payment> findOrderByUserId(Long userId, Long orderId);

}
