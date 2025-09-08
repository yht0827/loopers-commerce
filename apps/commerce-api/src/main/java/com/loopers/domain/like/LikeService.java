package com.loopers.domain.like;

import java.util.List;

import org.springframework.stereotype.Service;

import com.loopers.domain.product.ProductId;
import com.loopers.domain.user.UserId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;

	public Like likeProduct(final UserId userId, final ProductId productId) {

		// 이미 좋아요를 눌렀는지 확인
		if (likeRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "이미 좋아요를 누른 상품입니다.");
		}

		// 좋아요 정보 저장
		Like productLike = new Like(userId, productId);

		return likeRepository.save(productLike);
	}

	public void unlikeProduct(final UserId userId, final ProductId productId) {
		Like productLike = likeRepository.findByUserIdAndProductId(userId, productId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "좋아요 정보를 찾을 수 없습니다."));

		likeRepository.delete(productLike);
	}

	public List<Like> getAllLikedProductIds(final UserId userId) {
		return likeRepository.getAllLikedByUserId(userId)
			.stream()
			.map(Like::from)
			.toList();
	}
}
