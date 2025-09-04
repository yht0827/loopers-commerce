package com.loopers.application.payment;

import com.loopers.domain.order.event.OrderCreatedEvent;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentInfo;

public sealed interface PaymentResult {

	record Success(
		String transactionKey,
		String orderId,
		CardType cardType,
		String cardNo,
		Long amount,
		PaymentInfo paymentInfo
	) implements PaymentResult {
		public static Success from(PaymentInfo info) {
			return new Success(
				info.transactionKey(), info.orderId(), info.cardType(),
				info.cardNo(), info.amount(), info
			);
		}
	}

	record Failure(
		String orderId,
		String userId,
		Long amount,
		String reason,
		Exception exception,
		OrderCreatedEvent orderEvent
	) implements PaymentResult {
		public static Failure from(OrderCreatedEvent event, Exception exception) {
			return new Failure(
				event.orderId(), event.userId(), event.totalAmount(),
				exception.getMessage(), exception, event
			);
		}
	}

	static PaymentResult success(PaymentInfo info) {
		return Success.from(info);
	}

	static PaymentResult failure(OrderCreatedEvent event, Exception exception) {
		return Failure.from(event, exception);
	}
}
