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
public class UsersServiceIntegrationTest {
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
            UsersModel result = usersService.join(command);

            // assert
            ArgumentCaptor<UsersModel> captor = ArgumentCaptor.forClass(UsersModel.class);
            verify(usersJpaRepository, times(1)).save(captor.capture());
            UsersModel savedUser = captor.getValue();

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
}
