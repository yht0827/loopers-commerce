package com.loopers.application.like;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.loopers.domain.common.ProductId;
import com.loopers.domain.like.event.ProductLikedEvent;
import com.loopers.domain.like.event.ProductUnLikedEvent;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.ProductAggregate;
import com.loopers.domain.product.ProductAggregateRepository;
import com.loopers.domain.product.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeEventHandler {

	private final ProductService productService;
	private final ProductAggregateRepository productAggregateRepository;

	@EventListener
	public void handleProductLiked(ProductLikedEvent event) {
		// Update Query로 원자적 처리
		boolean success = productAggregateRepository.tryIncrementLikeCount(event.productId());

		if (!success) {
			// 집계 테이블에 없으면 생성 후 재시도
			createNewProductAggregate(event.productId());
			productAggregateRepository.tryIncrementLikeCount(event.productId());
			log.info("새로운 ProductAggregate 생성 및 좋아요 증가: {}", event.productId());
		} else {
			log.debug("좋아요 수 증가 완료: {}", event.productId());
		}

		// 캐시 무효화
		productService.evictProductCache(event.productId());
		productService.evictProductListCache();
	}

	@EventListener
	public void handleProductUnliked(ProductUnLikedEvent event) {
		productAggregateRepository.tryDecrementLikeCount(event.productId());

		// 캐시 무효화
		productService.evictProductCache(event.productId());
		productService.evictProductListCache();
	}

	private void createNewProductAggregate(Long productId) {
		ProductAggregate newAggregate = ProductAggregate.builder()
			.productId(new ProductId(productId))
			.likeCount(LikeCount.Zero())
			.build();

		productAggregateRepository.save(newAggregate);
	}

}
