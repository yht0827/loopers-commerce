package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.domain.product.ProductId;
import com.loopers.domain.user.UserId;
import com.loopers.support.error.CoreException;

public class LikeTest {

	@DisplayName("좋아요 모델을 생성할 때,")
	@Nested
	class Create {
		@DisplayName("필드 값들이 모두 주어지면, 정상적으로 생성된다.")
		@Test
		void createLikeModel_success() {
			// arrange
			Like like = Like.builder()
				.userId(UserId.of("yht0827"))
				.productId(ProductId.of(100L))
				.build();

			// act and assert
			assertThat(like)
				.isNotNull()
				.satisfies(l -> {
					assertThat(l).isNotNull();
					assertThat(l.getUserId().getUserId()).isEqualTo("yht0827");
					assertThat(l.getProductId().getProductId()).isEqualTo(100L);
				});
		}

		@DisplayName("사용자 ID가 null이면, 예외가 발생한다.")
		@Test
		void createLike_withNullUserId_throwsException() {
			// assert
			assertThatThrownBy(() -> Like.builder()
				.userId(UserId.of(null))
				.productId(ProductId.of(100L))
				.build())
				.isInstanceOf(CoreException.class)
				.hasMessage("사용자 ID는 필수입니다.");
		}

		@DisplayName("제품 ID가 0 이하면, 예외가 발생한다.")
		@Test
		void createLike_withZeroProductId_throwsException() {
			// assert
			assertThatThrownBy(() -> Like.builder()
				.userId(UserId.of("yht0827"))
				.productId(ProductId.of(0L))
				.build())
				.isInstanceOf(CoreException.class)
				.hasMessage("제품 ID는 비어있을 수 없습니다.");
		}
	}
}
