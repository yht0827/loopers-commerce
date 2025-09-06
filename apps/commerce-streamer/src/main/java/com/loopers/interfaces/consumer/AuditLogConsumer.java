package com.loopers.interfaces.consumer;

import static com.loopers.config.kafka.KafkaConfig.*;
import static com.loopers.config.kafka.KafkaGroups.*;
import static com.loopers.config.kafka.KafkaTopics.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.loopers.application.AuditLogFacade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogConsumer {

	private final AuditLogFacade auditLogFacade;

	@KafkaListener(
		topics = {ORDER, PAYMENT_SUCCESS, PAYMENT_FAIL, PRODUCT_LIKE, PRODUCT_UNLIKE},
		groupId = AuditLog,
		containerFactory = BATCH_LISTENER
	)
	public void handleAuditLog(
		List<ConsumerRecord<String, Object>> records,
		Acknowledgment acknowledgment) {

		Map<String, Long> topicCounts = records.stream()
			.collect(Collectors.groupingBy(
				ConsumerRecord::topic,
				Collectors.counting()
			));

		log.info("감사 로그 배치 수신 - 총 {}건, 토픽별: {}", records.size(), topicCounts);
		try {
			auditLogFacade.handleAuditLog(records);

			acknowledgment.acknowledge(); // manual ack

			log.info("AuditLog 로그 배치 처리 완료");
		} catch (Exception e) {
			log.error("AuditLog 로그 배치 처리 실패: {}", e.getMessage(), e);
		}
	}
}
