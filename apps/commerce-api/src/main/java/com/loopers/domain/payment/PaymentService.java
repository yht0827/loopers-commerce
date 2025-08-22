package com.loopers.domain.payment;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

	public void createPayment(final PaymentData.PaymentRequest data) {
		Payment payment = data.toEntity();
		paymentRepository.save(payment);
	}

	public PaymentInfo updatePaymentStatus(PaymentData.PaymentRequest data) {
		Payment payment = data.toEntity();

		payment.processPaymentSuccess(data.orderId());

		Payment savedPayment = paymentRepository.save(payment);
		return PaymentInfo.from(savedPayment);
	}
}
