package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointTest {

    @DisplayName("회원 모델을 생성할 때, ")
    @Nested
    class Create {

        @DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
        @Test
        void failWhenChargingWithZeroOrNegativeAmount() {
            // arrange
            Long userId = 0L;
            Long balance = -1L;

            // act
            CoreException result = assertThrows(CoreException.class, () -> new Point(userId, balance));

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
