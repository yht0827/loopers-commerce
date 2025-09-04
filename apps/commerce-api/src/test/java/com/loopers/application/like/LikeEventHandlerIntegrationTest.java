package com.loopers.application.like;

import static org.awaitility.Awaitility.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeId;
import com.loopers.domain.like.LikeRepository;
import com.loopers.domain.product.ProductAggregateService;
import com.loopers.domain.product.ProductService;

@SpringBootTest
class LikeEventHandlerIntegrationTest {

	@Autowired
	private LikeFacade likeFacade;

	@Autowired
	private LikeRepository likeRepository;

	@MockitoSpyBean
	private ProductAggregateService productAggregateService;

	@MockitoSpyBean
	private ProductService productService;

	@Test
	@DisplayName("상품 좋아요 시 이벤트 핸들러가 정상적으로 처리되는지 확인")
	void testLikeProductEventHandling() {
		// Given
		Long userId = 1L;
		Long productId = 100L;

		given(productAggregateService.incrementLikeCount(productId)).willReturn(true);

		// When
		likeFacade.likeProduct(userId, productId);

		// Then - 비동기 이벤트 처리 대기
		await()
			.atMost(5, TimeUnit.SECONDS)
			.pollInterval(Duration.ofMillis(50))
			.untilAsserted(() -> {
				verify(productAggregateService, times(1)).incrementLikeCount(productId);
				verify(productService, times(1)).evictProductRelatedCaches(productId);
			});
	}

	@Test
	@DisplayName("상품 좋아요 취소 시 이벤트 핸들러가 정상적으로 처리되는지 확인")
	void testUnlikeProductEventHandling() {
		// Given
		Long userId = 1L;
		Long productId = 100L;

		given(productAggregateService.decrementLikeCount(productId)).willReturn(true);

		// When
		likeFacade.unlikeProduct(userId, productId);

		// Then - 비동기 이벤트 처리 대기
		await()
			.atMost(5, TimeUnit.SECONDS)
			.pollInterval(Duration.ofMillis(50))
			.untilAsserted(() -> {
				verify(productAggregateService, times(1)).decrementLikeCount(productId);
				verify(productService, times(1)).evictProductRelatedCaches(productId);
			});
	}

	@Test
	@DisplayName("좋아요 수 업데이트 실패 시 Aggregate 생성 후 재시도")
	void testLikeProductWithAggregateCreation() {
		// Given
		Long userId = 1L;
		Long productId = 200L;

		// 첫 번째 호출은 실패, 두 번째 호출은 성공
		given(productAggregateService.incrementLikeCount(productId))
			.willReturn(false)  // 첫 번째 호출 실패
			.willReturn(true);  // 재시도 호출 성공

		// When
		likeFacade.likeProduct(userId, productId);

		// Then - 비동기 이벤트 처리 대기
		await()
			.atMost(5, TimeUnit.SECONDS)
			.pollInterval(Duration.ofMillis(50))
			.untilAsserted(() -> {
				// createIfNotExists가 호출되었는지 확인
				verify(productAggregateService, times(1)).createIfNotExists(productId);
				// incrementLikeCount가 2번 호출되었는지 확인 (실패 + 재시도)
				verify(productAggregateService, times(2)).incrementLikeCount(productId);
				// 캐시 무효화가 호출되었는지 확인
				verify(productService, times(1)).evictProductRelatedCaches(productId);
			});
	}

	@Test
	@DisplayName("좋아요 취소 시 실패하면 캐시 무효화가 호출되지 않는지 확인")
	void testUnlikeProductFailure() {
		// Given
		Long userId = 1L;
		Long productId = 300L;

		Like like = new Like(new LikeId(userId, productId));
		likeRepository.save(like);

		given(productAggregateService.decrementLikeCount(productId)).willReturn(false);

		// When
		likeFacade.unlikeProduct(userId, productId);

		// Then - 비동기 이벤트 처리 대기
		await()
			.atMost(5, TimeUnit.SECONDS)
			.pollInterval(Duration.ofMillis(50))
			.untilAsserted(() -> {
				verify(productAggregateService, times(1)).decrementLikeCount(productId);
				// 실패했으므로 캐시 무효화가 호출되지 않아야 함
				verify(productService, times(0)).evictProductRelatedCaches(anyLong());
			});
	}
}
