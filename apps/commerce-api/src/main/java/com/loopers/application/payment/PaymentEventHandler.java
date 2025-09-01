package com.loopers.application.payment;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.event.PaymentCompletedEvent;
import com.loopers.domain.payment.event.PaymentEvent;
import com.loopers.domain.payment.event.PaymentFailedEvent;
import com.loopers.domain.payment.event.PaymentRequestEvent;
import com.loopers.domain.platform.event.DataPlatformEvent;
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
	private final ObjectMapper objectMapper;

	@EventListener
	public void handlePaymentRequest(DomainApplicationEvent<PaymentRequestEvent> event) {

		PaymentRequestEvent paymentRequestEvent = event.getPayload();
		processPaymentRequest(paymentRequestEvent);
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void processPaymentRequest(PaymentRequestEvent event) {
		try {

			PaymentCommand.CreatePayment paymentCommand = new PaymentCommand.CreatePayment(event.userId(), event.orderId(),
				event.cardType(), event.cardNo(), event.amount(), event.callbackUrl());

			PaymentInfo paymentInfo = paymentProcessor.process(paymentCommand);

			PaymentCompletedEvent paymentCompletedEvent = PaymentCompletedEvent.create(event.orderId(), event.userId(),
				paymentInfo.transactionKey());
			eventPublisher.publish(paymentCompletedEvent);

			sendPaymentDataToPlatform(paymentCompletedEvent);

		} catch (Exception e) {
			log.error("결제 처리 실패: 주문 ID {}", event.orderId(), e);

			PaymentFailedEvent paymentFailedEvent = PaymentFailedEvent.create(event.orderId(), event.userId(), null,
				event.amount(), e);
			eventPublisher.publish(paymentFailedEvent);

			sendPaymentDataToPlatform(paymentFailedEvent);
		}
	}

	public void sendPaymentDataToPlatform(PaymentCompletedEvent event) {
		sendPaymentDataToPlatform(event, event.transactionKey(), "결제 성공");
	}

	public void sendPaymentDataToPlatform(PaymentFailedEvent event) {
		String aggregateId = event.transactionKey() != null ? event.transactionKey() : event.orderId();
		sendPaymentDataToPlatform(event, aggregateId, "결제 실패");
	}

	public void sendPaymentDataToPlatform(PaymentEvent paymentEvent, String aggregateId, String eventType) {
		try {
			String paymentData = objectMapper.writeValueAsString(paymentEvent);

			DataPlatformEvent dataPlatformEvent = DataPlatformEvent.fromPayment(aggregateId, paymentData);

			eventPublisher.publish(dataPlatformEvent);
			log.info("{} 데이터 플랫폼 전송 이벤트 발행: {}", eventType, paymentEvent.getOrderId());

		} catch (JsonProcessingException e) {
			log.error("{} 데이터 JSON 변환 실패: {}", eventType, paymentEvent.getOrderId(), e);
		}
	}
}
