package com.loopers.application.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentFacade {
	private final PaymentService paymentService;

	@Transactional
	public PaymentResult processCallback(final PaymentCommand.ProcessCallback command) {
		PaymentInfo paymentInfo = paymentService.processCallback(command);
		return PaymentResult.from(paymentInfo);
	}
}
