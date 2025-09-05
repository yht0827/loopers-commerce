package com.loopers.infrastructure.like;

import java.time.LocalDate;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.loopers.config.event.ProductLikeAggregationEvent;
import com.loopers.config.event.ProductLikedEvent;
import com.loopers.config.event.ProductUnlikedEvent;
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
			// 감사용 이벤트 발행
			ProductLikedEvent auditEvent = ProductLikedEvent.create(userId, productId);
			kafkaTemplate.send(KafkaTopics.PRODUCT_LIKE, productId.toString(), auditEvent);

			// 집계용 이벤트 발행
			ProductLikeAggregationEvent aggregationEvent =
				ProductLikeAggregationEvent.createLike(productId, userId);
			String partitionKey = productId + ":" + LocalDate.now();
			kafkaTemplate.send(KafkaTopics.LIKE_AGGREGATION, partitionKey, aggregationEvent);

			log.debug("좋아요 이벤트 발행 완료 - userId: {}, productId: {}", userId, productId);

		} catch (Exception e) {
			log.error("좋아요 이벤트 발행 실패: userId={}, productId={}", userId, productId, e);
		}
	}

	public void publishUnlike(String userId, Long productId) {
		try {
			// 감사용 이벤트 발행
			ProductUnlikedEvent auditEvent = ProductUnlikedEvent.create(userId, productId);
			kafkaTemplate.send(KafkaTopics.PRODUCT_UNLIKE, productId.toString(), auditEvent);

			// 집계용 이벤트 발행
			ProductLikeAggregationEvent aggregationEvent =
				ProductLikeAggregationEvent.createUnlike(productId, userId);
			String partitionKey = productId + ":" + LocalDate.now();
			kafkaTemplate.send(KafkaTopics.LIKE_AGGREGATION, partitionKey, aggregationEvent);

			log.debug("좋아요 취소 이벤트 발행 완료 - userId: {}, productId: {}", userId, productId);

		} catch (Exception e) {
			log.error("좋아요 취소 이벤트 발행 실패: userId={}, productId={}", userId, productId, e);
		}
	}
}
