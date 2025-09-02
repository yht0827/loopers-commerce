package com.loopers.application.like;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.product.ProductAggregateService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.event.ProductLikedEvent;
import com.loopers.domain.product.event.ProductUnLikedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeEventHandler {

	private final ProductService productService;
	private final ProductAggregateService productAggregateService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleProductLiked(ProductLikedEvent event) {
		// Update Query로 원자적 처리
		boolean success = productAggregateService.incrementLikeCount(event.productId());

		if (!success) {
			log.warn("좋아요 수 업데이트 실패 - 상품 없음: {}", event.productId());
			productAggregateService.createIfNotExists(event.productId());
			productAggregateService.incrementLikeCount(event.productId());
		}

		// 캐시 무효화
		productService.evictProductCache(event.productId());
		productService.evictProductListCache();
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleProductUnliked(ProductUnLikedEvent event) {
		boolean success = productAggregateService.decrementLikeCount(event.productId());

		if (!success) {
			log.warn("좋아요 취소 업데이트 실패: productId={}", event.productId());
			return;
		}

		// 캐시 무효화
		productService.evictProductCache(event.productId());
		productService.evictProductListCache();
	}
}
