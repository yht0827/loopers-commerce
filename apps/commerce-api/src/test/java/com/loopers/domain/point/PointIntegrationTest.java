package com.loopers.domain.point;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.domain.common.UserId;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
public class PointIntegrationTest {

	@Autowired
	private PointCommandService pointCommandService;

	@Autowired
	private PointQueryService pointQueryService;

	@MockitoSpyBean
	private PointJpaRepository pointJpaRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	private static final String TEST_USER_ID = "yht0827";
	private static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(1000L);
	private static final BigDecimal CHARGE_AMOUNT = BigDecimal.valueOf(500L);

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	private Point createTestPoint() {
		return Point.builder()
			.userId(new UserId(TEST_USER_ID))
			.balance(new Balance(INITIAL_AMOUNT))
			.build();
	}

	@Nested
	class Read {
		@DisplayName("포인트를 조회할 때,")
		@Nested
		class GetPoint {

			@DisplayName("해당 ID의 회원이 존재할 경우, 보유 포인트가 반환된다.")
			@Test
			void shouldReturnPoint_whenUserExists() {
				// arrange
				Point point = createTestPoint();
				pointJpaRepository.save(point);

				Point chargePoint = Point.builder()
					.userId(new UserId(TEST_USER_ID))
					.balance(new Balance(CHARGE_AMOUNT))
					.build();
				pointCommandService.chargePoint(chargePoint);

				// act
				Point result = pointQueryService.getPoint(TEST_USER_ID);

				// assert
				assertThat(result).isNotNull();
				assertThat(result.getBalance().balance()).isEqualByComparingTo(INITIAL_AMOUNT.add(CHARGE_AMOUNT));
			}

			@DisplayName("해당 ID의 회원이 존재하지 않을 경우, null이 반환된다.")
			@Test
			void shouldReturnNull_whenUserDoesNotExist() {
				// arrange
				String nonExistentUserId = "nonExistent";

				// act and assert
				assertThrows(CoreException.class, () -> pointQueryService.getPoint(nonExistentUserId));
			}
		}
	}

	@Nested
	class Create {
		@DisplayName("포인트를 충전할 때,")
		@Nested
		class ChargePoint {

			@DisplayName("존재하지 않는 유저 ID로 충전을 시도한 경우, 실패한다.")
			@Test
			void shouldThrowException_whenUserDoesNotExist() {
				// arrange
				String nonExistentUserId = "nonExistent";
				Point chargePoint = Point.builder()
					.userId(new UserId(nonExistentUserId))
					.balance(new Balance(CHARGE_AMOUNT))
					.build();

				// act and assert
				assertThrows(
					CoreException.class,
					() -> pointCommandService.chargePoint(chargePoint)
				);
			}
		}
	}
}


