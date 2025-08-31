package com.loopers.application.order;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.domain.payment.event.PaymentRequestEvent;
import com.loopers.domain.platform.event.DataPlatformEvent;
import com.loopers.support.event.DomainApplicationEvent;
import com.loopers.support.event.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventHandler {

	private final EventPublisher eventPublisher;
	private final ObjectMapper objectMapper;

	@EventListener
	public void handleOrderCreated(DomainApplicationEvent event) {
		if (event.isPayloadOfType(OrderCreatedEvent.class)) {
			return;
		}

		OrderCreatedEvent orderCreatedEvent = event.getPayload(OrderCreatedEvent.class);
		processOrderCreated(orderCreatedEvent);
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void processOrderCreated(OrderCreatedEvent event) {

		sendOrderDataToPlatform(event);

		if (event.paymentMetadata() != null) {
			PaymentRequestEvent paymentRequestEvent = PaymentRequestEvent.create(
				event.orderId(),
				event.userId(),
				event.totalAmount(),
				event.paymentMetadata().cardType(),
				event.paymentMetadata().cardNo(),
				event.paymentMetadata().callbackUrl()
			);

			eventPublisher.publish(paymentRequestEvent);
		}

		log.info("주문 생성 후 처리 완료: {}", event.orderId());
	}

	public void sendOrderDataToPlatform(OrderCreatedEvent event) {
		try {

			String orderData = objectMapper.writeValueAsString(event);

			DataPlatformEvent dataPlatformEvent = DataPlatformEvent.fromOrder(
				event.orderId(),
				orderData
			);

			eventPublisher.publish(dataPlatformEvent);
			log.info("주문 데이터 플랫폼 전송 이벤트 발행: {}", event.orderId());

		} catch (JsonProcessingException e) {
			log.error("주문 데이터 JSON 변환 실패: {}", event.orderId(), e);
		}
	}
}
