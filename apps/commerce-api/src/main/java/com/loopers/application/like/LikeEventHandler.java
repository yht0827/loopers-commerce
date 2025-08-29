package com.loopers.application.like;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.like.event.ProductLikedEvent;
import com.loopers.domain.like.event.ProductUnLikedEvent;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeEventHandler {

	private final ProductService productService;
	private final ProductRepository productRepository;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleProductLiked(ProductLikedEvent event) {

		Product product = productRepository.findById(event.productId())
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		LikeCount likeCount = product.getLikeCount().increase();
		product.updateLikeCount(likeCount);

		// 캐시 무효화
		productService.evictProductCache(event.productId());
		productService.evictProductListCache();
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleProductUnliked(ProductUnLikedEvent event) {

		Product product = productRepository.findById(event.productId())
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		// 상품의 좋아요 수 감소
		LikeCount likeCount = product.getLikeCount().decrease();
		product.updateLikeCount(likeCount);

		// 캐시 무효화
		productService.evictProductCache(event.productId());
		productService.evictProductListCache();
	}

}
