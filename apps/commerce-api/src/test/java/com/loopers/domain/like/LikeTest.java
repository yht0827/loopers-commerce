package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.*;

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
			LikeId likeId = new LikeId(1L, 100L);

			// act
			Like like = Like.builder().targetId(likeId).build();

			// assert
			assertThat(like)
				.isNotNull()
				.satisfies(l -> {
					assertThat(l.getTargetId()).isNotNull();
					assertThat(like.getTargetId().getUserId()).isEqualTo(1L);
					assertThat(like.getTargetId().getTargetId()).isEqualTo(100L);
				});
		}

		@DisplayName("사용자 ID가 null이면, 예외가 발생한다.")
		@Test
		void createLike_withNullUserId_throwsException() {
			// assert
			assertThatThrownBy(() -> new LikeId(null, 100L))
				.isInstanceOf(CoreException.class)
				.hasMessage("사용자 ID는 비어있을 수 없습니다.");
		}

		@DisplayName("대상 ID가 0 이하면, 예외가 발생한다.")
		@Test
		void createLike_withZeroTargetId_throwsException() {
			// assert
			assertThatThrownBy(() -> new LikeId(1L, 0L))
				.isInstanceOf(CoreException.class)
				.hasMessage("좋아요 대상 ID는 비어있을 수 없습니다.");
		}
	}
}
