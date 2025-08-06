package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.application.point.port.in.PointCommand;
import com.loopers.domain.common.UserId;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;

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

			Point point = pointJpaRepository.save(new Point(new UserId(userId), balance));

			// act
			Point result = pointService.getPoint(point.getUserId().userId());

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

			Point point = pointJpaRepository.save(new Point(new UserId(nonExistentUserId), balance));

			// act and assert
			assertThrows(CoreException.class, () -> pointService.getPoint(nonExistentUserId));
		}
	}

	@DisplayName("포인트를 생성할 때,")
	@Nested
	class Post {
		@DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
		@Test
		void shouldFailWhenChargingWithNonExistentUserId() {
			// arrange
			Long nonExistentUserId = 999L;
			Long amount = 1000L;
			PointCommand command = new PointCommand(nonExistentUserId, amount);

			// act and assert
			assertThrows(CoreException.class, () -> pointService.charge(command));
		}

	}

}
