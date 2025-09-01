package com.loopers.application.order;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.order.OrderService;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.event.PaymentCompletedEvent;
import com.loopers.domain.payment.event.PaymentFailedEvent;
import com.loopers.support.event.DomainApplicationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusUpdateHandler {

	private final OrderService orderService;

	@EventListener
	public void handlePaymentCompleted(DomainApplicationEvent<PaymentCompletedEvent> event) {

		PaymentCompletedEvent paymentCompletedEvent = event.getPayload();
		processPaymentCompleted(paymentCompletedEvent);
	}

	@EventListener
	public void handlePaymentFailed(DomainApplicationEvent<PaymentFailedEvent> event) {

		PaymentFailedEvent paymentFailedEvent = event.getPayload();
		processPaymentFailed(paymentFailedEvent);
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	private void processPaymentCompleted(PaymentCompletedEvent event) {
		try {

			orderService.updateOrderStatus(event.orderId(), OrderStatus.CONFIRMED);

			log.info("주문 상태 업데이트 완료: {} -> CONFIRMED", event.orderId());

		} catch (Exception e) {
			log.error("결제 완료 후 주문 상태 업데이트 실패: 주문 ID {}", event.orderId(), e);
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	private void processPaymentFailed(PaymentFailedEvent event) {
		try {

			orderService.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED);

			log.info("주문 상태 업데이트 완료: {} -> CANCELLED", event.orderId());

		} catch (Exception e) {
			log.error("결제 실패 후 주문 상태 업데이트 실패: 주문 ID {}", event.orderId(), e);
		}
	}
}
