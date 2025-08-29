package com.loopers.domain.payment.event;

import java.time.LocalDateTime;

import com.loopers.domain.payment.CardType;

public record PaymentRequestEvent(
	String orderId,
	String userId,
	Long amount,
	CardType cardType,
	String cardNo,
	String callbackUrl,
	LocalDateTime occurredAt
) implements PaymentEvent {

	public static final String EVENT_TYPE = "PAYMENT_REQUESTED";

	public static PaymentRequestEvent create(String orderId, String userId, Long amount,
		CardType cardType, String cardNo, String callbackUrl) {
		return new PaymentRequestEvent(orderId, userId, amount, cardType, cardNo, callbackUrl, LocalDateTime.now());
	}

	@Override
	public String getAggregateId() {
		return orderId;
	}

	@Override
	public LocalDateTime getOccurredAt() {
		return occurredAt;
	}

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

	@Override
	public String getOrderId() {
		return orderId;
	}
}
