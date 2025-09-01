package com.loopers.application.like;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.like.LikeInfo;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.event.ProductLikedEvent;
import com.loopers.domain.like.event.ProductUnLikedEvent;
import com.loopers.support.event.EventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class LikeFacade {

	private final LikeService likeService;
	private final EventPublisher eventPublisher;

	public LikeResult likeProduct(Long userId, Long productId) {
		LikeInfo likeInfo = likeService.likeProduct(userId, productId);

		ProductLikedEvent event = ProductLikedEvent.create(userId.toString(), productId);
		eventPublisher.publish(event);

		return LikeResult.from(likeInfo);
	}

	public void unlikeProduct(Long userId, Long productId) {
		likeService.unlikeProduct(userId, productId);

		ProductUnLikedEvent event = ProductUnLikedEvent.create(userId.toString(), productId);
		eventPublisher.publish(event);
	}

	@Transactional(readOnly = true)
	public List<LikeResult> getLikedProductList(Long userId) {
		List<LikeInfo> likeInfos = likeService.getAllLikedProductIds(userId);

		return likeInfos.stream()
			.map(LikeResult::from)
			.toList();
	}

}
