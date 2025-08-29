package com.loopers.application.payment;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.event.PaymentCompletedEvent;
import com.loopers.domain.payment.event.PaymentFailedEvent;
import com.loopers.domain.payment.event.PaymentRequestEvent;
import com.loopers.support.event.DomainApplicationEvent;
import com.loopers.support.event.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventHandler {

	private final PaymentProcessor paymentProcessor;
	private final EventPublisher eventPublisher;

	@EventListener
	public void handlePaymentRequest(DomainApplicationEvent event) {
		if (event.isPayloadOfType(PaymentRequestEvent.class)) {
			return;
		}

		PaymentRequestEvent paymentRequestEvent = event.getPayload(PaymentRequestEvent.class);
		processPaymentRequest(paymentRequestEvent);
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void processPaymentRequest(PaymentRequestEvent event) {
		try {

			PaymentCommand.CreatePayment paymentCommand = new PaymentCommand.CreatePayment(
				event.userId(), event.orderId(), event.cardType(), event.cardNo(), event.amount(), event.callbackUrl());

			PaymentInfo paymentInfo = paymentProcessor.process(paymentCommand);

			PaymentCompletedEvent paymentCompletedEvent = PaymentCompletedEvent.create(
				event.orderId(), event.userId(), paymentInfo.transactionKey());

			eventPublisher.publish(paymentCompletedEvent);
		} catch (Exception e) {
			log.error("결제 처리 실패: 주문 ID {}", event.orderId(), e);

			PaymentFailedEvent paymentFailedEvent = PaymentFailedEvent.create(
				event.orderId(),
				event.userId(),
				null,
				event.amount(),
				e
			);
			eventPublisher.publish(paymentFailedEvent);
		}
	}
}
