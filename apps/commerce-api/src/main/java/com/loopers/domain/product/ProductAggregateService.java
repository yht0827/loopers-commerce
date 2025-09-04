package com.loopers.domain.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.common.ProductId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductAggregateService {

	private final ProductAggregateRepository productAggregateRepository;

	public boolean incrementLikeCount(final Long productId) {
		return productAggregateRepository.incrementLikeCount(productId);
	}

	public boolean decrementLikeCount(final Long productId) {
		return productAggregateRepository.decrementLikeCount(productId);
	}

	public void createIfNotExists(final Long productId) {
		// UPSERT 패턴 또는 존재 여부 체크 후 생성
		if (!productAggregateRepository.existsByProductId(productId)) {
			ProductAggregate productAggregate = ProductAggregate.builder()
				.productId(new ProductId(productId))
				.likeCount(LikeCount.Zero())
				.build();

			productAggregateRepository.save(productAggregate);
		}

	}

}
