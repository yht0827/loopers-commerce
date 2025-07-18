package com.loopers.domain.point;

import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PointServiceIntegrationTest {
    @Autowired
    private PointService pointService;

    @Autowired
    private PointJpaRepository pointJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트를 조회할 때,")
    @Nested
    class Get {

        @DisplayName("해당 ID의 회원이 존재하면, 보유 포인트를 반환한다.")
        @Test
        void returnsPoints_whenUserExists() {
            // arrange
            Long userId = 1L;
            Long balance = 1000L;

            PointModel pointModel = pointJpaRepository.save(new PointModel(userId, balance));

            // act
            PointModel result = pointService.getPoint(pointModel.getUserId());

            // assert
            assertAll(
                    () -> assertThat(result.getBalance()).isEqualTo(balance)
            );
        }


        @DisplayName("해당 ID의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void shouldThrowException_whenUserDoesNotExist() {
            // arrange
            Long nonExistentUserId = 999L;
            Long balance = 1000L;

            PointModel pointModel = pointJpaRepository.save(new PointModel(nonExistentUserId, balance));

            // act and assert
            assertThrows(CoreException.class, () -> pointService.getPoint(nonExistentUserId));
        }
    }
}
