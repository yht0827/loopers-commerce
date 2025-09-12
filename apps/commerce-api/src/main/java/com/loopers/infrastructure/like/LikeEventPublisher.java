package com.loopers.infrastructure.like;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.loopers.config.event.ProductAggregationEvent;
import com.loopers.config.kafka.KafkaTopics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeEventPublisher {

	private final KafkaTemplate<Object, Object> kafkaTemplate;

	public void publishLike(String userId, Long productId) {
		try {

			ProductAggregationEvent event = ProductAggregationEvent.createLike(productId, userId);

			kafkaTemplate.send(KafkaTopics.PRODUCT_LIKE, productId.toString(), event);

			log.debug("좋아요 이벤트 발행 완료 - userId: {}, productId: {}", userId, productId);

		} catch (Exception e) {
			log.error("좋아요 이벤트 발행 실패: userId={}, productId={}", userId, productId, e);
		}
	}

	public void publishUnlike(String userId, Long productId) {
		try {

			ProductAggregationEvent event = ProductAggregationEvent.createUnlike(productId, userId);

			kafkaTemplate.send(KafkaTopics.PRODUCT_LIKE, productId.toString(), event);

			log.debug("좋아요 취소 이벤트 발행 완료 - userId: {}, productId: {}", userId, productId);

		} catch (Exception e) {
			log.error("좋아요 취소 이벤트 발행 실패: userId={}, productId={}", userId, productId, e);
		}
	}
}
