package com.loopers.domain.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.domain.common.UserId;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
public class UserIntegrationTest {
	@Autowired
	private UserCommandService userCommandService;

	@Autowired
	private UserQueryService userQueryService;

	@MockitoSpyBean
	private UserJpaRepository userJpaRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	private static final String TEST_USER_ID = "yht0827";
	private static final String TEST_NAME = "양희태";
	private static final String TEST_EMAIL = "yht0827@naver.com";
	private static final String TEST_BIRTHDAY = "1999-01-01";

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	private User createTestUser() {
		return User.builder()
			.userId(new UserId(TEST_USER_ID))
			.name(new UserName(TEST_NAME))
			.email(new Email(TEST_EMAIL))
			.birthday(new Birthday(TEST_BIRTHDAY))
			.gender(Gender.M)
			.build();
	}

	private void assertUserEquals(User user) {
		assertAll(
			() -> assertThat(user).isNotNull(),
			() -> assertThat(user.getUserId().userId()).isEqualTo(TEST_USER_ID),
			() -> assertThat(user.getName().name()).isEqualTo(TEST_NAME),
			() -> assertThat(user.getEmail().email()).isEqualTo(TEST_EMAIL),
			() -> assertThat(user.getBirthday().birthday()).isEqualTo(TEST_BIRTHDAY),
			() -> assertThat(user.getGender()).isEqualTo(Gender.M)
		);
	}

	@DisplayName("유저를 생성할 때,")
	@Nested
	class CreateUser {

		@DisplayName("올바른 정보로 회원 가입시, User 저장이 수행된다.")
		@Test
		void shouldSaveUser_whenValidUserCommandProvided() {
			// arrange
			User user = createTestUser();

			// act
			User result = userCommandService.createUser(user);

			// assert
			ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
			verify(userJpaRepository, times(1)).save(captor.capture());
			User savedUser = captor.getValue();

			assertUserEquals(result);
			assertUserEquals(savedUser);
		}

		@DisplayName("이미 가입된 ID로 회원가입 시도 시, 예외가 발생한다.")
		@Test
		void shouldThrowException_whenUserIdAlreadyExists() {
			// arrange
			User user = createTestUser();
			userCommandService.createUser(user);

			User duplicateUser = createTestUser();

			// act and assert
			assertThrows(
				CoreException.class, () -> userCommandService.createUser(duplicateUser));
		}
	}

	@DisplayName("유저를 조회할 때,")
	@Nested
	class GetUser {

		@DisplayName("해당 ID의 회원이 존재할 경우, 회원 정보가 반환된다.")
		@Test
		void shouldReturnUserInfo_whenUserExists() {
			// arrange
			User user = createTestUser();
			User savedUser = userCommandService.createUser(user);

			// act
			User foundUser = userQueryService.getUser(savedUser.getUserId().userId());

			// assert
			assertUserEquals(foundUser);
		}

		@DisplayName("해당 ID의 회원이 존재하지 않을 경우, 예외가 발생한다.")
		@Test
		void shouldThrowException_whenUserDoesNotExist() {
			// arrange
			String nonExistentUserId = "nonExistent";

			// act and assert
			assertThrows(CoreException.class, () -> userQueryService.getUser(nonExistentUserId));
		}
	}
}
