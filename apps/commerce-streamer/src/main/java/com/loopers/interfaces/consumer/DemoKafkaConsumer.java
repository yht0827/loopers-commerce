package com.loopers.interfaces.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.loopers.config.kafka.KafkaConfig;
import com.loopers.config.kafka.KafkaTopics;

@Component
public class DemoKafkaConsumer {

	@KafkaListener(
		topics = KafkaTopics.ORDER,
		containerFactory = KafkaConfig.BATCH_LISTENER
	)
	public void demoListener(
		List<ConsumerRecord<String, Object>> messages,
		Acknowledgment acknowledgment
	) {
		System.out.println(messages);
		acknowledgment.acknowledge(); // manual ack
	}
}
