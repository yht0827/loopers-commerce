package com.loopers.testcontainers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class KafkaTestContainersConfig {

	private static final KafkaContainer kafka =
		new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"));

	static {
		kafka.start();
	}

	@DynamicPropertySource
	static void registerKafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
		registry.add("spring.kafka.admin.properties.bootstrap.servers", kafka::getBootstrapServers);
	}

}
