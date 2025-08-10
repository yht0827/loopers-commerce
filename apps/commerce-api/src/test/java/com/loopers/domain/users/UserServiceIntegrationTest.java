package com.loopers.domain.users;

import com.loopers.application.users.port.in.UsersCommand;
import com.loopers.infrastructure.users.UsersJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceIntegrationTest {
    @Autowired
    private UsersService usersService;

    @MockitoSpyBean
    private UsersJpaRepository usersJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("유저를 생성할 때,")
    @Nested
    class Post {
        @DisplayName("회원 가입시, User 저장이 수행된다. ( spy 검증 )")
        @Test
        void shouldSaveUser_whenSignUp() {
            // arrange
            String userId = "yht0827";
            String name = "양희태";
            String email = "yht0827@naver.com";
            String birthday = "1999-01-01";
            String gender = "M";

            UsersCommand command = new UsersCommand(userId, name, email, birthday, gender);

            // act
            User result = usersService.join(command);

            // assert
            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(usersJpaRepository, times(1)).save(captor.capture());
            User savedUser = captor.getValue();

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getUserId()).isEqualTo(userId),
                    () -> assertThat(result.getName()).isEqualTo(name),
                    () -> assertThat(result.getEmail()).isEqualTo(email),
                    () -> assertThat(result.getBirthday()).isEqualTo(birthday),
                    () -> assertThat(result.getGender()).isEqualTo(gender),

                    () -> assertThat(savedUser.getUserId()).isEqualTo(userId),
                    () -> assertThat(savedUser.getName()).isEqualTo(name),
                    () -> assertThat(savedUser.getEmail()).isEqualTo(email),
                    () -> assertThat(savedUser.getBirthday()).isEqualTo(birthday),
                    () -> assertThat(savedUser.getGender()).isEqualTo(gender)
            );
        }

        @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
        @Test
        void shouldFailToJoin_whenUserIdAlreadyExists() {
            // arrange
            String name = "양희태";
            String email = "yht0827@naver.com";
            String birthday = "1999-01-01";
            String gender = "M";
            usersService.join(new UsersCommand("yht0827", name, email, birthday, gender));

            UsersCommand duplicateUserCommand = new UsersCommand("yht0827", name, email, birthday, gender);

            // act and assert
            assertThrows(CoreException.class, () -> usersService.join(duplicateUserCommand));
        }
    }

    @DisplayName("유저를 조회할 때,")
    @Nested
    class Get {

        @DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void shouldReturnUserInfo_whenUserExists() {
            // arrange
            String userId = "yht0827";
            String name = "양희태";
            String email = "yht0827@naver.com";
            String birthday = "1999-01-01";
            String gender = "M";
            User savedUser = usersService.join(new UsersCommand(userId, name, email, birthday, gender));

            // act
            User foundUser = usersService.me(savedUser.getUserId());

            // assert
            assertAll(
                    () -> assertThat(foundUser).isNotNull(),
                    () -> assertThat(foundUser.getUserId()).isEqualTo(userId),
                    () -> assertThat(foundUser.getName()).isEqualTo(name),
                    () -> assertThat(foundUser.getEmail()).isEqualTo(email),
                    () -> assertThat(foundUser.getBirthday()).isEqualTo(birthday),
                    () -> assertThat(foundUser.getGender()).isEqualTo(gender)
            );

        }


        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void shouldThrowException_whenUserDoesNotExist() {
            // arrange
            String nonExistentUserId = "nonExistentUser";

            // act & assert
            // 존재하지 않는 사용자를 조회 시 CoreException 이 발생하는 것을 검증합니다.
            assertThrows(CoreException.class, () -> usersService.me(nonExistentUserId));

        }
    }
}
