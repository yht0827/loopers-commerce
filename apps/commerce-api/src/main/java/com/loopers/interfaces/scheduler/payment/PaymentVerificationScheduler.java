package com.loopers.interfaces.scheduler.payment;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentGatewayService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentVerificationScheduler {

	private final PaymentGatewayService paymentGatewayService;

	@Scheduled(fixedDelay = 60000)
	public void verifyPendingPayments() {
		paymentGatewayService.verifyPendingPayments();
	}
}
