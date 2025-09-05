package com.loopers.domain.product.event;

import java.time.LocalDateTime;

public record ProductOutOfStockEvent(
	Long productId,
	LocalDateTime occurredAt
) implements ProductEvent {

	public static final String EVENT_TYPE = "PRODUCT_OUT_OF_STOCK";

	public static ProductOutOfStockEvent create(Long productId) {
		return new ProductOutOfStockEvent(productId, LocalDateTime.now());
	}

	@Override
	public String getAggregateId() {
		return productId.toString();
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
	public String getCorrelationId() {
		return "";
	}

	@Override
	public Long getProductId() {
		return productId;
	}
}
