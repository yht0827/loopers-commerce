package com.loopers.infrastructure.order;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.loopers.config.event.OrderCreatedEvent;
import com.loopers.config.kafka.KafkaTopics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventPublisher {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void publish(String userId, String orderId, Long totalAmount) {
		try {
			OrderCreatedEvent event = OrderCreatedEvent.create(userId, orderId, totalAmount);
			kafkaTemplate.send(KafkaTopics.ORDER, orderId, event);
		} catch (Exception e) {
			log.error("주문 생성 이벤트 카프카 전송 실패: orderId={}", orderId, e);
		}
	}
}
