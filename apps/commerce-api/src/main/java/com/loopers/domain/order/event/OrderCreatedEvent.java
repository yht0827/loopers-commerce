package com.loopers.domain.order.event;

import java.time.LocalDateTime;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.payment.CardType;

public record OrderCreatedEvent(
	String userId,
	String orderId,
	Long totalAmount,
	PaymentMetadata paymentMetadata,
	LocalDateTime occurredAt
) implements OrderEvent {

	public static final String EVENT_TYPE = "ORDER_CREATED";

	public static OrderCreatedEvent create(String userId, String orderId, Long totalAmount,
		PaymentMetadata paymentMetadata) {
		return new OrderCreatedEvent(userId, orderId, totalAmount, paymentMetadata, LocalDateTime.now());
	}

	public static OrderCreatedEvent from(OrderInfo orderInfo, PaymentMetadata paymentMetadata) {
		return new OrderCreatedEvent(
			orderInfo.userId(),
			orderInfo.orderId(),
			orderInfo.totalPrice(),
			paymentMetadata,
			LocalDateTime.now()
		);
	}

	public record PaymentMetadata(
		CardType cardType,
		String cardNo,
		String callbackUrl
	) {
		public static PaymentMetadata of(CardType cardType, String cardNo, String callbackUrl) {
			return new PaymentMetadata(cardType, cardNo, callbackUrl);
		}
	}

	@Override
	public String getAggregateId() {
		return orderId;
	}

	@Override
	public String getOrderId() {
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
}
