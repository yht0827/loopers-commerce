package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class UserTest {
	@DisplayName("회원을 생성할 때, ")
	@Nested
	class Create {
		@DisplayName("필드 값들이 모두 주어지면, 정상적으로 생성된다.")
		@Test
		void createsUsersModel_whenFieldsAreProvided() {
			// arrange
			String userId = "yht0827";
			String name = "양희태";
			String email = "yht0827@naver.com";
			String birthday = "1999-01-01";

			// act
			User user = User.builder()
				.userId(new UserId(userId))
				.name(new UserName(name))
				.email(new Email(email))
				.birthday(new Birthday(birthday))
				.gender(Gender.M)
				.build();

			// assert
			assertThat(user)
				.isNotNull()
				.satisfies(u -> {
					assertThat(u.getUserId().getUserId()).isEqualTo(userId);
					assertThat(u.getName().getName()).isEqualTo(name);
					assertThat(u.getEmail().getEmail()).isEqualTo(email);
					assertThat(u.getBirthday().getBirthday()).isEqualTo(birthday);
					assertThat(u.getGender()).isEqualTo(Gender.M);
				});
		}

		@DisplayName("사용자 이름이 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
		@Test
		void throwsBadRequestException_whenUserNameIsInvalid() {
			// arrange
			String userId = "yht0827";
			String invalidName = "test123!@#"; // 숫자와 특수문자 포함
			String email = "yht0827@naver.com";
			String birthday = "1999-01-01";

			// act and assert
			CoreException result = assertThrows(CoreException.class, () ->
				User.builder()
					.userId(new UserId(userId))
					.name(new UserName(invalidName))
					.email(new Email(email))
					.birthday(new Birthday(birthday))
					.gender(Gender.M)
					.build()
			);

			assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
			assertThat(result.getCustomMessage()).contains(USER_NAME_INVALID_FORMAT.format(10));
		}

		@DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
		@Test
		void throwsBadRequestException_whenEmailIsInvalid() {
			// arrange
			String userId = "yht0827";
			String name = "양희태";
			String invalidEmail = "yht0827@naver";
			String birthday = "1999-01-01";

			// act and assert
			CoreException result = assertThrows(CoreException.class, () ->
				User.builder()
					.userId(new UserId(userId))
					.name(new UserName(name))
					.email(new Email(invalidEmail))
					.birthday(new Birthday(birthday))
					.gender(Gender.M)
					.build()
			);

			assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
			assertThat(result.getCustomMessage()).isEqualTo(EMAIL_INVALID_FORMAT.getMessage());
		}

		@DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
		@Test
		void throwsBadRequestException_whenBirthdayIsInvalid() {
			// arrange
			String userId = "yht0827";
			String name = "양희태";
			String email = "yht0827@naver.com";
			String invalidBirthday = "19990101";

			// act and assert
			CoreException result = assertThrows(CoreException.class, () ->
				User.builder()
					.userId(new UserId(userId))
					.name(new UserName(name))
					.email(new Email(email))
					.birthday(new Birthday(invalidBirthday))
					.gender(Gender.M)
					.build()
			);

			assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
			assertThat(result.getCustomMessage()).isEqualTo(BIRTHDAY_INVALID_FORMAT.getMessage());
		}
	}
}
