package com.loopers.support.redis;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.loopers.domain.users.User;
import com.loopers.utils.RedisCleanUp;

@SpringBootTest
public class RedisTest {

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisCleanUp redisCleanUp;

	@Autowired
	public RedisTest(RedisTemplate<String, Object> redisTemplate, RedisCleanUp redisCleanUp) {
		this.redisTemplate = redisTemplate;
		this.redisCleanUp = redisCleanUp;
	}

	@BeforeEach
	public void setUp() {
		redisCleanUp.truncateAll();
	}

	@Test
	@DisplayName("Redis 연결 테스트")
	public void redisConnectionTest() {
		redisTemplate.opsForValue().set("connection-test", "success");
		String result = (String)redisTemplate.opsForValue().get("connection-test");

		assertThat(result).isEqualTo("success");
	}

	@Test
	@DisplayName("JSON 직렬화 테스트 - User 객체를 Redis에 저장")
	public void jsonObjectSerializationTest() {
		User user = User.builder()
			.userId("testuser1")
			.name("홍길동")
			.email("hong@example.com")
			.birthday("1990-01-01")
			.gender("M")
			.build();

		redisTemplate.opsForValue().set("user:1", user);

		Object storedUser = redisTemplate.opsForValue().get("user:1");
		assertThat(storedUser).isNotNull();
		assertThat(storedUser).isInstanceOf(User.class);
	}

	@Test
	@DisplayName("JSON 역직렬화 테스트 - Redis에서 User 객체 조회")
	public void jsonObjectDeserializationTest() {
		User originalUser = User.builder()
			.userId("testuser2")
			.name("이순신")
			.email("lee@example.com")
			.birthday("1985-10-15")
			.gender("M")
			.build();

		redisTemplate.opsForValue().set("user:deserialize", originalUser);

		// When: Redis에서 객체를 조회
		Object result = redisTemplate.opsForValue().get("user:deserialize");

		// Then: 역직렬화된 객체 검증
		assertThat(result).isNotNull();
		assertThat(result).isInstanceOf(User.class);

		if (result instanceof User user) {
			assertThat(user.getName()).isEqualTo("이순신");
			assertThat(user.getEmail()).isEqualTo("lee@example.com");
			assertThat(user.getUserId()).isEqualTo("testuser2");
			assertThat(user.getBirthday()).isEqualTo("1985-10-15");
			assertThat(user.getGender()).isEqualTo("M");
		}
	}

	@Test
	@DisplayName("JSON Hash 연산 테스트 - Hash 구조에서 User 객체 저장/조회")
	public void jsonHashOperationTest() {
		User user1 = User.builder()
			.userId("kimcs")
			.name("김철수")
			.email("kim@example.com")
			.birthday("1985-05-15")
			.gender("M")
			.build();

		User user2 = User.builder()
			.userId("leeyh")
			.name("이영희")
			.email("lee@example.com")
			.birthday("1988-03-20")
			.gender("F")
			.build();

		redisTemplate.opsForHash().put("users", "user1", user1);
		redisTemplate.opsForHash().put("users", "user2", user2);

		Object retrievedUser1 = redisTemplate.opsForHash().get("users", "user1");
		Object retrievedUser2 = redisTemplate.opsForHash().get("users", "user2");

		assertThat(retrievedUser1).isNotNull().isInstanceOf(User.class);
		assertThat(retrievedUser2).isNotNull().isInstanceOf(User.class);

		User retrievedUserObj1 = (User)retrievedUser1;
		User retrievedUserObj2 = (User)retrievedUser2;

		assertThat(retrievedUserObj1.getName()).isEqualTo("김철수");
		assertThat(retrievedUserObj1.getUserId()).isEqualTo("kimcs");
		assertThat(retrievedUserObj2.getName()).isEqualTo("이영희");
		assertThat(retrievedUserObj2.getUserId()).isEqualTo("leeyh");
	}

}
