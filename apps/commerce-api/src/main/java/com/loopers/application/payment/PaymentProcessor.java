package com.loopers.application.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentData;
import com.loopers.domain.payment.PaymentGatewayService;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentProcessor {
	private final PaymentService paymentService;
	private final PaymentGatewayService paymentGatewayService;

	public PaymentInfo process(PaymentCommand.CreatePayment command) {
		PaymentData.PaymentRequest data = command.toData();

		// 결제 생성
		paymentService.createPayment(data);

		// PG 결제 요청 (fallback 포함)
		PaymentInfo.transaction pgResponse = paymentGatewayService.requestPaymentGateWay(data);

		// PG 응답 기반으로 결제 정보 업데이트
		return paymentService.updatePaymentStatus(data, pgResponse);
	}
}
