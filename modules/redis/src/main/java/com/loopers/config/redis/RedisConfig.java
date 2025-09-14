package com.loopers.config.redis;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.ReadFrom;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {
	private static final String CONNECTION_MASTER = "redisConnectionMaster";
	public static final String REDIS_TEMPLATE_MASTER = "redisTemplateMaster";

	private final RedisProperties redisProperties;

	public RedisConfig(RedisProperties redisProperties) {
		this.redisProperties = redisProperties;
	}

	@Primary
	@Bean
	public LettuceConnectionFactory defaultRedisConnectionFactory() {
		int database = redisProperties.database();
		RedisNodeInfo master = redisProperties.master();
		List<RedisNodeInfo> replicas = redisProperties.replicas();
		return lettuceConnectionFactory(
			database, master, replicas,
			b -> b.readFrom(ReadFrom.REPLICA_PREFERRED)
		);
	}

	@Qualifier(CONNECTION_MASTER)
	@Bean
	public LettuceConnectionFactory masterRedisConnectionFactory() {
		int database = redisProperties.database();
		RedisNodeInfo master = redisProperties.master();
		List<RedisNodeInfo> replicas = redisProperties.replicas();
		return lettuceConnectionFactory(
			database, master, replicas,
			b -> b.readFrom(ReadFrom.MASTER)
		);
	}

	@Primary
	@Bean
	public RedisTemplate<String, Object> defaultRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		return defaultRedisTemplate(redisTemplate, lettuceConnectionFactory);
	}

	@Qualifier(REDIS_TEMPLATE_MASTER)
	@Bean
	public RedisTemplate<String, Object> masterRedisTemplate(
		@Qualifier(CONNECTION_MASTER) LettuceConnectionFactory lettuceConnectionFactory
	) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		return defaultRedisTemplate(redisTemplate, lettuceConnectionFactory);
	}

	private LettuceConnectionFactory lettuceConnectionFactory(
		int database,
		RedisNodeInfo master,
		List<RedisNodeInfo> replicas,
		Consumer<LettuceClientConfiguration.LettuceClientConfigurationBuilder> customizer
	) {
		LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();
		if (customizer != null)
			customizer.accept(builder);
		LettuceClientConfiguration clientConfig = builder.build();
		RedisStaticMasterReplicaConfiguration masterReplicaConfig = new RedisStaticMasterReplicaConfiguration(master.host(),
			master.port());
		masterReplicaConfig.setDatabase(database);
		for (RedisNodeInfo r : replicas) {
			masterReplicaConfig.addNode(r.host(), r.port());
		}
		return new LettuceConnectionFactory(masterReplicaConfig, clientConfig);
	}

	private <K, V> RedisTemplate<K, V> defaultRedisTemplate(
		RedisTemplate<K, V> template,
		LettuceConnectionFactory connectionFactory
	) {
		StringRedisSerializer keySerializer = new StringRedisSerializer();
		GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
		template.setKeySerializer(keySerializer);
		template.setValueSerializer(valueSerializer);
		template.setHashKeySerializer(keySerializer);
		template.setHashValueSerializer(valueSerializer);
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	@Bean
	public StringRedisTemplate rankingStringRedisTemplate(LettuceConnectionFactory defaultRedisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(defaultRedisConnectionFactory);
		return template;
	}
}
