package com.loopers.application.order;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.domain.payment.event.PaymentRequestEvent;
import com.loopers.support.event.DomainApplicationEvent;
import com.loopers.support.event.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventHandler {

	private final EventPublisher eventPublisher;

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
}
