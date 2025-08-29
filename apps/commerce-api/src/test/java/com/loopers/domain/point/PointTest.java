package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.domain.common.UserId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class PointTest {

	@DisplayName("포인트를 생성할 때, ")
	@Nested
	class Create {

		@DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
		@Test
		void failWhenChargingWithZeroOrNegativeAmount() {
			// arrange
			String userId = "yht0827";
			BigDecimal balance = BigDecimal.valueOf(-1L);

			// act
			CoreException result = assertThrows(CoreException.class, () -> new Point(new UserId(userId), new Balance(balance)));

			// assert
			assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}
	}
}
