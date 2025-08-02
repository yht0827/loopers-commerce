package com.loopers.domain.users;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    @DisplayName("회원 모델을 생성할 때, ")
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
            String gender = "M";

            // act
            User user = new User(userId, name, email, birthday, gender);

            // assert
            assertAll(
                    () -> assertThat(user.getId()).isNotNull(),
                    () -> assertThat(user.getName()).isEqualTo(name),
                    () -> assertThat(user.getEmail()).isEqualTo(email),
                    () -> assertThat(user.getBirthday()).isEqualTo(birthday),
                    () -> assertThat(user.getGender()).isEqualTo(gender)
            );
        }

        @DisplayName("유저 ID가 영문 및 숫자 10자 이내 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenUserIdIsInvalid() {
            // arrange
            String userId = "yht08271111111";
            String name = "양희태";
            String email = "yht0827@naver.com";
            String birthday = "1999-01-01";
            String gender = "M";

            // act
            CoreException result = assertThrows(CoreException.class, () -> new User(userId, name, email, birthday, gender));

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            assertThat(result.getCustomMessage()).isEqualTo("아이디는 영문, 숫자를 포함하여 10자 이내여야 합니다.");
        }

        @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenEmailIsInvalid() {
            // arrange
            String userId = "yht0827";
            String name = "양희태";
            String email = "yht0827@naver";
            String birthday = "1999-01-01";
            String gender = "M";

            // act
            CoreException result = assertThrows(CoreException.class, () -> new User(userId, name, email, birthday, gender));

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            assertThat(result.getCustomMessage()).isEqualTo("유효하지 않은 이메일 형식입니다.");
        }

        @DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenBirthdayIsInvalid() {
            // arrange
            String userId = "yht0827";
            String name = "양희태";
            String email = "yht0827@naver.com";
            String birthday = "19990101";
            String gender = "M";

            // act
            CoreException result = assertThrows(CoreException.class, () -> new User(userId, name, email, birthday, gender));

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            assertThat(result.getCustomMessage()).isEqualTo("생년월일은 yyyy-MM-dd 형식이어야 합니다.");

        }
    }
}
