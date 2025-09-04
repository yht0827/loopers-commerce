package com.loopers.support.event;

import lombok.Getter;

@Getter
public enum EventType {
	ORDER_CREATED("ORDER_CREATED"),
	PAYMENT_COMPLETED("PaymentCompletedEvent"),
	PAYMENT_FAILED("PaymentFailedEvent"),
	PRODUCT_LIKED("ProductLikedEvent"),
	PRODUCT_UNLIKED("ProductUnLikedEvent"),
	DATA_PLATFORM("DataPlatformEvent");

	private final String className;

	EventType(String className) {
		this.className = className;
	}
}
