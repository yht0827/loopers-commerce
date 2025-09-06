package com.loopers.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.config.event.ProductLikeAggregationEvent;
import com.loopers.domain.ProductMetricData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMetricProcessor {

	public List<ProductMetricData> parseEvents(List<ProductLikeAggregationEvent> events) {
		return events.stream()
			.map(this::parseEvent)
			.toList();
	}

	public ProductMetricData parseEvent(ProductLikeAggregationEvent event) {
		try {
			return ProductMetricData.of(
				event.eventId(),
				event.productId(),
				event.action(),
				event.userId(),
				event.eventDate(),
				event.occurredAt(),
				null
			);
		} catch (Exception e) {
			log.error("이벤트 파싱 실패 - EventId: {}", event.eventId(), e);
			throw e;
		}
	}
}
