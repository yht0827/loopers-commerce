package com.loopers.interfaces.api.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserV1ApiE2ETest {

	private final TestRestTemplate testRestTemplate;
	private final DatabaseCleanUp databaseCleanUp;

	@Autowired
	public UserV1ApiE2ETest(
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

			UserDto.V1.UserRequest request = new UserDto.V1.UserRequest(userId, name, email, birthday, gender);
			HttpEntity<UserDto.V1.UserRequest> requestEntity = new HttpEntity<>(request);

			String requestUrl = "/api/v1/users";

			// act
			ParameterizedTypeReference<ApiResponse<UserDto.V1.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserDto.V1.UserResponse>> response =
				testRestTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, responseType);

			// assert
			UserDto.V1.UserResponse responseData = response.getBody().data();

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

			UserDto.V1.UserRequest request = new UserDto.V1.UserRequest(userId, name, email, birthday, null);
			HttpEntity<UserDto.V1.UserRequest> requestEntity = new HttpEntity<>(request);

			String requestUrl = "/api/v1/users";

			// act
			ResponseEntity<Object> response =
				testRestTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, Object.class);

			// assert
			assertTrue(response.getStatusCode().is4xxClientError());
		}
	}

	@DisplayName("GET /api/v1/users")
	@Nested
	class Get {
		@DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
		@Test
		void returnsUserInfo_whenIdExists() {
			// arrange
			String userId = "yht0827";
			String name = "양희태";
			String email = "yht0827@naver.com";
			String birthday = "1999-01-01";
			String gender = "M";
			UserDto.V1.UserRequest postRequest = new UserDto.V1.UserRequest(userId, name, email, birthday, gender);
			HttpEntity<UserDto.V1.UserRequest> postRequestEntity = new HttpEntity<>(postRequest);
			String postRequestUrl = "/api/v1/users";

			ParameterizedTypeReference<ApiResponse<UserDto.V1.UserResponse>> postResponseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserDto.V1.UserResponse>> postResponse =
				testRestTemplate.exchange(postRequestUrl, HttpMethod.POST, postRequestEntity, postResponseType);
			String createdUserId = postResponse.getBody().data().userId();

			// act
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-USER-ID", createdUserId);
			HttpEntity<Object> getRequestEntity = new HttpEntity<>(headers);

			ParameterizedTypeReference<ApiResponse<UserDto.V1.UserResponse>> getResponseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserDto.V1.UserResponse>> getResponse =
				testRestTemplate.exchange("/api/v1/users/me", HttpMethod.GET, getRequestEntity, getResponseType);

			// assert
			UserDto.V1.UserResponse responseData = getResponse.getBody().data();
			assertAll(
				() -> assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK),
				() -> assertThat(responseData.userId()).isEqualTo(userId),
				() -> assertThat(responseData.name()).isEqualTo(name),
				() -> assertThat(responseData.email()).isEqualTo(email),
				() -> assertThat(responseData.birthday()).isEqualTo(birthday),
				() -> assertThat(responseData.gender()).isEqualTo(gender)
			);
		}

		@DisplayName("존재하지 않는 ID 로 조회할 경우, 404 Not Found 응답을 반환한다.")
		@Test
		void returnsNotFound_whenIdDoesNotExist() {
			// arrange
			long nonExistentId = 999L;
			String requestUrl = "/api/v1/users/" + nonExistentId;

			// act
			ResponseEntity<Object> response =
				testRestTemplate.exchange(requestUrl, HttpMethod.GET, null, Object.class);

			// assert
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		}
	}
}
