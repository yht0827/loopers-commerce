package com.loopers.config.kafka;

public final class KafkaTopics {
	public static final String ORDER = "order-events";
	public static final String PAYMENT_SUCCESS = "payment-success-events";
	public static final String PAYMENT_FAIL = "payment-fail-events";
	public static final String PRODUCT_LIKE = "product-like-events";

	private KafkaTopics() {
	}
}

