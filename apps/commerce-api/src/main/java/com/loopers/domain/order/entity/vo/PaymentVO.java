package com.loopers.domain.order.entity.vo;

import java.math.BigDecimal;

import com.loopers.domain.order.entity.Payment;

public record PaymentVO(Long id, Long orderId, String orderNumber, BigDecimal amount, String method, String status) {
	public static PaymentVO from(Payment payment) {
		return new PaymentVO(
			payment.getId(),
			payment.getOrderId().orderId(),
			payment.getOrderNumber().orderNumber(),
			payment.getDetail().amount(),
			payment.getDetail().method(),
			payment.getStatus().name()
		);
	}
}
