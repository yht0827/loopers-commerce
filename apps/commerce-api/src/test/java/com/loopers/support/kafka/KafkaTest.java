package com.loopers.support.kafka;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaTest {

	@Autowired
	private KafkaProperties kafkaProperties;

	@Test
	public void producerpropertiesshouldcontainacksallandidempotencetrue() {
		Map<String, Object> props = kafkaProperties.buildProducerProperties();

		Assertions.assertEquals("all", props.get(ProducerConfig.ACKS_CONFIG));

		Object idempotence = props.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG);

		Assertions.assertTrue(Boolean.TRUE.equals(idempotence) || "true".equalsIgnoreCase(String.valueOf(idempotence)));
	}

}
