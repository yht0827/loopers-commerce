package com.loopers.application.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentFacade {
	private final PaymentProcessor paymentProcessor;
	private final PaymentService paymentService;

	@Transactional
	public PaymentResult createPayment(final PaymentCommand.CreatePayment command) {
		PaymentInfo paymentInfo = paymentProcessor.process(command);
		return PaymentResult.from(paymentInfo);
	}

	@Transactional
	public PaymentResult processCallback(final PaymentCommand.ProcessCallback command) {
		PaymentInfo paymentInfo = paymentService.processCallback(command);
		return PaymentResult.from(paymentInfo);
	}
}
