package com.loopers.testcontainers;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

@Configuration
public class RedisTestContainersConfig {
	private static final RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:latest"));

	static {
		redisContainer.start();
		System.setProperty("datasource.redis.database", "0");
		System.setProperty("datasource.redis.master.host", redisContainer.getHost());
		System.setProperty("datasource.redis.master.port", String.valueOf(redisContainer.getFirstMappedPort()));
		System.setProperty("datasource.redis.replicas[0].host", redisContainer.getHost());
		System.setProperty("datasource.redis.replicas[0].port", String.valueOf(redisContainer.getFirstMappedPort()));
	}
}

