package com.loopers.application.like;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductId;
import com.loopers.domain.product.event.ProductLikedEvent;
import com.loopers.domain.product.event.ProductUnLikedEvent;
import com.loopers.domain.user.UserId;
import com.loopers.infrastructure.like.LikeEventPublisher;
import com.loopers.support.event.EventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class LikeFacade {

	private final LikeService likeService;
	private final EventPublisher eventPublisher;
	private final LikeEventPublisher likeEventPublisher;

	public LikeResult likeProduct(final String userId, final Long productId) {

		final UserId uid = UserId.of(userId);
		final ProductId pid = ProductId.of(productId);

		Like like = likeService.likeProduct(uid, pid);

		ProductLikedEvent event = ProductLikedEvent.create(userId, productId);
		eventPublisher.publish(event);
		likeEventPublisher.publishLike(event.userId(), event.productId());

		return LikeResult.from(like);
	}

	public void unlikeProduct(final String userId, final Long productId) {

		final UserId uid = UserId.of(userId);
		final ProductId pid = ProductId.of(productId);

		likeService.unlikeProduct(uid, pid);

		ProductUnLikedEvent event = ProductUnLikedEvent.create(userId, productId);
		eventPublisher.publish(event);
		likeEventPublisher.publishUnlike(event.userId(), event.productId());
	}

	@Transactional(readOnly = true)
	public List<LikeResult> getLikedProductList(final String userId) {

		final UserId uid = UserId.of(userId);

		List<Like> likeList = likeService.getAllLikedProductIds(uid);

		return likeList.stream()
			.map(LikeResult::from)
			.toList();
	}

}
