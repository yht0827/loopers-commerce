package com.loopers.infrastructure.payment;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.loopers.config.event.PaymentFailEvent;
import com.loopers.config.event.PaymentSuccessEvent;
import com.loopers.config.kafka.KafkaTopics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventPublisher {

	private final KafkaTemplate<Object, Object> kafkaTemplate;

	public void publishSuccess(String orderId, Long amount) {
		try {
			PaymentSuccessEvent event = PaymentSuccessEvent.create(orderId, amount);
			kafkaTemplate.send(KafkaTopics.PAYMENT_SUCCESS, orderId, event);
		} catch (Exception e) {
			log.error("결제 성공 이벤트 카프카 전송 실패: orderId={}", orderId, e);
		}
	}

	public void publishFail(String orderId, Long amount, String failureReason) {
		try {
			PaymentFailEvent event = PaymentFailEvent.create(orderId, amount, failureReason);
			kafkaTemplate.send(KafkaTopics.PAYMENT_SUCCESS, orderId, event);
		} catch (Exception e) {
			log.error("결제 실패 이벤트 카프카 전송 실패: orderId={}", orderId, e);
		}
	}
}
