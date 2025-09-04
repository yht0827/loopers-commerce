package com.loopers.application.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.domain.payment.PaymentData;
import com.loopers.domain.payment.PaymentGatewayService;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentProcessor {
	private final PaymentService paymentService;
	private final PaymentGatewayService paymentGatewayService;

	public PaymentInfo process(PaymentCommand.CreatePayment command) {
		PaymentData.PaymentRequest data = command.toData();

		paymentService.createPayment(data);

		PaymentInfo.transaction pgResponse = paymentGatewayService.requestPaymentGateWay(data);

		return paymentService.updatePaymentStatus(data, pgResponse);
	}

	public PaymentResult processOrderPayment(OrderCreatedEvent orderEvent) {
		if (orderEvent.paymentMetadata() != null) {
			throw new CoreException(ErrorType.NOT_FOUND, "결제 정보 없음");
		}

		try {
			PaymentCommand.CreatePayment command = PaymentCommand.CreatePayment.fromOrderEvent(orderEvent);
			PaymentInfo paymentInfo = process(command);

			return PaymentResult.success(paymentInfo);
		} catch (Exception e) {
			return PaymentResult.failure(orderEvent, e);
		}
	}
}
