package com.loopers.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.loopers.utils.RedisCleanUp;

@SpringBootTest
public class RedisTest {

	private final RedisTemplate<String, String> redisTemplate;

	private final RedisCleanUp redisCleanUp;

	@Autowired
	public RedisTest(RedisTemplate<String, String> redisTemplate, RedisCleanUp redisCleanUp) {
		this.redisTemplate = redisTemplate;
		this.redisCleanUp = redisCleanUp;
	}

	@BeforeEach
	public void setUp() {
		redisCleanUp.truncateAll();
	}

	@Test
	public void t1() {
		System.out.println(redisTemplate.opsForValue().get("foo"));
		redisTemplate.opsForValue().set("foo", "bar");
	}

	@Test
	public void t2() {
		System.out.println(redisTemplate.opsForValue().get("foo"));
	}

}
