package com.loopers.interfaces.consumer;

import static com.loopers.config.kafka.KafkaConfig.*;
import static com.loopers.config.kafka.KafkaGroups.*;
import static com.loopers.config.kafka.KafkaTopics.*;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.loopers.application.ProductMetricFacade;
import com.loopers.config.event.ProductLikeAggregationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetricsConsumer {

	private final ProductMetricFacade productMetricFacade;

	@KafkaListener(
		topics = LIKE_AGGREGATION,
		groupId = Metrics,
		containerFactory = BATCH_LISTENER
	)
	public void handleLikeChangedEvent(
		List<ConsumerRecord<String, ProductLikeAggregationEvent>> records,
		Acknowledgment acknowledgment
	) {
		try {
			List<ProductLikeAggregationEvent> events = records.stream()
				.map(ConsumerRecord::value)
				.toList();

			productMetricFacade.handleLikeChangedEvents(events);

			acknowledgment.acknowledge();

			log.info("좋아요 메트릭 배치 처리 완료 - 총 {}건", events.size());
		} catch (Exception e) {
			log.error("좋아요 메트릭 배치 처리 실패", e);
		}
	}
}
