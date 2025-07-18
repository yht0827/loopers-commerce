package com.loopers.interfaces.api.point;

import com.loopers.domain.point.PointModel;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.port.in.PointRequest;
import com.loopers.interfaces.api.point.port.out.ChargeResponse;
import com.loopers.interfaces.api.point.port.out.PointResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;
    private final PointJpaRepository pointJpaRepository;

    @Autowired
    public PointV1ApiE2ETest(
            TestRestTemplate testRestTemplate,
            DatabaseCleanUp databaseCleanUp,
            PointJpaRepository pointJpaRepository

    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
        this.pointJpaRepository = pointJpaRepository;

    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("GET /api/v1/point")
    @Nested
    class Get {

        @Test
        @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
        void getPoint_success() {
            // arrange
            long userId = 1L;
            long balance = 1000L;

            pointJpaRepository.save(new PointModel(userId, balance));

            // act
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", String.valueOf(userId));
            HttpEntity<Object> request = new HttpEntity<>(headers);

            ParameterizedTypeReference<ApiResponse<PointResponse>> getResponseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<PointResponse>> response = testRestTemplate.exchange("/api/v1/point", HttpMethod.GET, request, getResponseType);

            // assert
            PointResponse responseData = response.getBody().data();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat((responseData.balance())).isEqualTo(balance);
        }

        @Test
        @DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
        void getPoint_fail_without_header() {
            // arrange
            HttpEntity<Object> requestEntity = new HttpEntity<>(new HttpHeaders());

            // act
            ResponseEntity<Object> response = testRestTemplate.exchange(
                    "/api/v1/point",
                    HttpMethod.GET,
                    requestEntity,
                    Object.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @DisplayName("POST /api/v1/charge")
    @Nested
    class Post {

        @Test
        @DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
        void chargePoint_success() {
            // arrange
            Long userId = 1L;
            Long initialBalance = 500L;
            Long chargeAmount = 1000L;

            pointJpaRepository.save(new PointModel(userId, initialBalance));

            PointRequest pointRequest = new PointRequest(userId, chargeAmount);
            HttpEntity<PointRequest> requestEntity = new HttpEntity<>(pointRequest);

            String requestUrl = "/api/v1/point/charge";

            // act
            ParameterizedTypeReference<ApiResponse<ChargeResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<ChargeResponse>> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, responseType);

            // assert
            ChargeResponse responseData = response.getBody().data();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().balance()).isEqualTo(initialBalance + chargeAmount);
        }

        @Test
        @DisplayName("존재하지 않는 유저로 요청할 경우, 404 Not Found 응답을 반환한다.")
        void chargePoint_fail_when_user_not_found() {
            // arrange
            long nonExistentUserId = 999L;
            long chargeAmount = 1000L;

            PointRequest pointRequest = new PointRequest(nonExistentUserId, chargeAmount);
            HttpEntity<PointRequest> requestEntity = new HttpEntity<>(pointRequest);

            String requestUrl = "/api/v1/point/charge";

            // act
            ParameterizedTypeReference<ApiResponse<ChargeResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<ChargeResponse>> response = testRestTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

    }
}
