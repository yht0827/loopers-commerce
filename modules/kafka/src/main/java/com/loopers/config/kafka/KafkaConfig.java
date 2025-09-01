package com.loopers.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableKafka
public class KafkaConfig {

	public static final String BATCH_LISTENER = "BATCH_LISTENER_DEFAULT";

	private static final int MAX_POLLING_SIZE = 3000; // read 3000 msg
	private static final int FETCH_MIN_BYTES = 1024 * 1024; // 1mb
	private static final int FETCH_MAX_WAIT_MS = 5 * 1000; // broker waiting time = 5s
	private static final int SESSION_TIMEOUT_MS = 60 * 1000; // session timeout = 1m
	private static final int HEARTBEAT_INTERVAL_MS = 20 * 1000; // heartbeat interval = 20s (1/3 of session_timeout)
	private static final int MAX_POLL_INTERVAL_MS = 2 * 60 * 1000; // max poll interval = 2m

	@Bean
	public ProducerFactory<Object, Object> producerFactory(KafkaProperties kafkaProperties) {
		Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	public ConsumerFactory<Object, Object> consumerFactory(KafkaProperties kafkaProperties) {
		Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public KafkaTemplate<Object, Object> kafkaTemplate(ProducerFactory<Object, Object> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public ByteArrayJsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
		return new ByteArrayJsonMessageConverter(objectMapper);
	}

	@Bean(BATCH_LISTENER)
	public ConcurrentKafkaListenerContainerFactory<Object, Object> defaultBatchListenerContainerFactory(
		KafkaProperties kafkaProperties, ByteArrayJsonMessageConverter converter) {

		Map<String, Object> consumerConfig = new HashMap<>(kafkaProperties.buildConsumerProperties());
		consumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLLING_SIZE);
		consumerConfig.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, FETCH_MIN_BYTES);
		consumerConfig.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, FETCH_MAX_WAIT_MS);
		consumerConfig.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, SESSION_TIMEOUT_MS);
		consumerConfig.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, HEARTBEAT_INTERVAL_MS);
		consumerConfig.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS);

		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig));
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		factory.setBatchMessageConverter(new BatchMessagingMessageConverter(converter));
		factory.setConcurrency(3);
		factory.setBatchListener(true);

		return factory;
	}
}
