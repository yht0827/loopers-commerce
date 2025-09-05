package com.loopers.infrastructure.like;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.loopers.config.event.ProductLikedEvent;
import com.loopers.config.event.ProductUnlikedEvent;
import com.loopers.config.kafka.KafkaTopics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeEventPublisher {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void publishLike(String userId, Long productId) {
		try {
			ProductLikedEvent event = ProductLikedEvent.create(userId, productId);
			kafkaTemplate.send(KafkaTopics.PRODUCT_LIKE, productId.toString(), event);
		} catch (Exception e) {
			log.error("상품 좋아요 이벤트 카프카 전송 실패: userId={}, productId={}", userId, productId, e);
		}
	}

	public void publishUnlike(String userId, Long productId) {
		try {
			ProductUnlikedEvent event = ProductUnlikedEvent.create(userId, productId);
			kafkaTemplate.send(KafkaTopics.PRODUCT_UNLIKE, productId.toString(), event);
		} catch (Exception e) {
			log.error("상품 좋아요 취소 이벤트 카프카 전송 실패: userId={}, productId={}", userId, productId, e);
		}
	}
}
