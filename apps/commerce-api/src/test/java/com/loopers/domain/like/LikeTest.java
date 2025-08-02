package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.error.CoreException;

public class LikeTest {

	@DisplayName("좋아요 모델을 생성할 때,")
	@Nested
	class Create {
		@DisplayName("필드 값들이 모두 주어지면, 정상적으로 생성된다.")
		@Test
		void createLikeModel_success() {
			// arrange
			LikeId likeId = LikeId.builder().userId(1L).targetId(100L).build();

			// act
			Like like = Like.builder().targetId(likeId).build();

			// assert
			assertAll(
				() -> assertThat(like.getTargetId()).isNotNull(),
				() -> assertThat(like.getTargetId().userId()).isEqualTo(1L),
				() -> assertThat(like.getTargetId().targetId()).isEqualTo(100L)
			);
		}

		@DisplayName("사용자 ID가 null이면, 예외가 발생한다.")
		@Test
		void createLike_withNullUserId_throwsException() {
			// assert
			assertThatThrownBy(() -> LikeId.builder().userId(null).targetId(100L).build())
				.isInstanceOf(CoreException.class)
				.hasMessage("사용자 ID는 비어있을 수 없습니다.");
		}

		@DisplayName("대상 ID가 0 이하면, 예외가 발생한다.")
		@Test
		void createLike_withZeroTargetId_throwsException() {
			// assert
			assertThatThrownBy(() -> LikeId.builder().userId(1L).targetId(0L).build())
				.isInstanceOf(CoreException.class)
				.hasMessage("좋아요 대상 ID는 비어있을 수 없습니다.");
		}
	}
}
