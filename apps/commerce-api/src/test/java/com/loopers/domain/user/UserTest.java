package com.loopers.domain.user;

import static com.loopers.support.error.ErrorMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@DisplayName("회원 단위 테스트")
public class UserTest {
	@DisplayName("회원을 생성할 때, ")
	@Nested
	class Create {
		@DisplayName("필드 값들이 모두 주어지면, 정상적으로 생성된다.")
		@Test
		void createsUsersModel_whenFieldsAreProvided() {
			// arrange
			String userId = "yht0827";
			String email = "yht0827@naver.com";
			String birthday = "1999-01-01";

			// act
			User user = User.create()
				.userId(new UserId(userId))
				.email(new Email(email))
				.birthday(new Birthday(birthday))
				.gender(Gender.M)
				.build();

			// assert
			assertThat(user)
				.isNotNull()
				.satisfies(u -> {
					assertThat(u.getUserId().getUserId()).isEqualTo(userId);
					assertThat(u.getEmail().getEmail()).isEqualTo(email);
					assertThat(u.getBirthday().getBirthday()).isEqualTo(birthday);
					assertThat(u.getGender()).isEqualTo(Gender.M);
				});
		}

		@DisplayName("사용자 ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, User 객체 생성에 실패한다.")
		@Test
		void throwsBadRequestException_whenUserNameIsInvalid() {
			// arrange
			String invalidUserId = "test123!@#"; // 숫자와 특수문자 포함
			String email = "yht0827@naver.com";
			String birthday = "1999-01-01";

			// act and assert
			CoreException result = assertThrows(CoreException.class, () ->
				User.create()
					.userId(new UserId(invalidUserId))
					.email(new Email(email))
					.birthday(new Birthday(birthday))
					.gender(Gender.M)
					.build()
			);

			assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
			assertThat(result.getCustomMessage()).contains(USER_NAME_INVALID_FORMAT.format(10));
		}

		@DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, User 객체 생성에 실패한다.")
		@Test
		void throwsBadRequestException_whenEmailIsInvalid() {
			// arrange
			String userId = "yht0827";
			String invalidEmail = "yht0827@naver";
			String birthday = "1999-01-01";

			// act and assert
			CoreException result = assertThrows(CoreException.class, () ->
				User.create()
					.userId(new UserId(userId))
					.email(new Email(invalidEmail))
					.birthday(new Birthday(birthday))
					.gender(Gender.M)
					.build()
			);

			assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
			assertThat(result.getCustomMessage()).isEqualTo(EMAIL_INVALID_FORMAT.getMessage());
		}

		@DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면, User 객체 생성에 실패한다.")
		@Test
		void throwsBadRequestException_whenBirthdayIsInvalid() {
			// arrange
			String userId = "yht0827";
			String email = "yht0827@naver.com";
			String invalidBirthday = "19990101";

			// act and assert
			CoreException result = assertThrows(CoreException.class, () ->
				User.create()
					.userId(new UserId(userId))
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
