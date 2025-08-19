package com.loopers.application.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentData;
import com.loopers.domain.payment.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

	private final PaymentService paymentService;

	public PaymentInfo createPayment(final PaymentCommand.CreatePayment command) {

		PaymentData.PaymentRequest paymentRequest = command.toData();
		return paymentService.processPayment(paymentRequest);
	}
}
