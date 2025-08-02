package com.loopers.application.like;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.domain.like.LikeInfo;
import com.loopers.domain.like.LikeService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeFacade {

	private final LikeService likeService;

	public LikeResult likeProduct(Long userId, Long productId) {
		LikeInfo likeInfo = likeService.likeProduct(userId, productId);
		return LikeResult.from(likeInfo);
	}

	public void unlikeProduct(Long userId, Long productId) {
		likeService.unlikeProduct(userId, productId);
	}

	public List<LikeResult> getLikedProductList(Long userId) {
		List<LikeInfo> likeInfos = likeService.getAllLikedProductIds(userId);

		return likeInfos.stream()
			.map(LikeResult::from)
			.toList();
	}

}
