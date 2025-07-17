package com.loopers.interfaces.api.users;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.users.port.in.UsersRequest;
import com.loopers.interfaces.api.users.port.out.UsersResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersV1ApiE2ETest {

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UsersV1ApiE2ETest(
            TestRestTemplate testRestTemplate,
            DatabaseCleanUp databaseCleanUp
    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Post {
        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsExampleInfo_whenValidIdIsProvided() {
            // arrange
            String userId = "yht0827";
            String name = "양희태";
            String email = "yht0827@naver.com";
            String birthday = "1999-01-01";
            String gender = "M";

            UsersRequest request = new UsersRequest(userId, name, email, birthday, gender);
            HttpEntity<UsersRequest> requestEntity = new HttpEntity<>(request);

            String requestUrl = "/api/v1/users";

            // act
            ParameterizedTypeReference<ApiResponse<UsersResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UsersResponse>> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, responseType);

            // assert
            UsersResponse responseData = response.getBody().data();

            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(responseData.id()).isNotNull(),
                    () -> assertThat(responseData.userId()).isEqualTo(userId),
                    () -> assertThat(responseData.name()).isEqualTo(name),
                    () -> assertThat(responseData.email()).isEqualTo(email),
                    () -> assertThat(responseData.birthday()).isEqualTo(birthday),
                    () -> assertThat(responseData.gender()).isEqualTo(gender)
            );
        }

        @DisplayName("회원 가입 시에 성별이 없을 경우, 400 Bad Request 응답을 반환한다.")
        @Test
        void returnsBadRequest_whenGenderIsNull() {
            // arrange
            String userId = "yht0827";
            String name = "양희태";
            String email = "yht0827@naver.com";
            String birthday = "1999-01-01";
            String gender = null;

            UsersRequest request = new UsersRequest(userId, name, email, birthday, gender);
            HttpEntity<UsersRequest> requestEntity = new HttpEntity<>(request);

            String requestUrl = "/api/v1/users";

            // act
            ResponseEntity<Object> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, Object.class);

            // assert
            assertTrue(response.getStatusCode().is4xxClientError());
        }

    }
}
