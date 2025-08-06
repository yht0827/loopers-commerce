package com.loopers.domain.like;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;
	private final ProductRepository productRepository;

	@Transactional
	public LikeInfo likeProduct(Long userId, Long productId) {
		// 상품 존재 여부 확인
		Product product = productRepository.findById(userId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		// 이미 좋아요를 눌렀는지 확인
		if (likeRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "이미 좋아요를 누른 상품입니다.");
		}

		// 상품의 좋아요 수
		LikeCount likeCount = product.getLikeCount().increase();
		product.updateLikeCount(likeCount);

		// 좋아요 정보 저장
		LikeId likeId = new LikeId(userId, productId);
		Like productLike = new Like(likeId);

		Like save = likeRepository.save(productLike);
		return LikeInfo.from(save);
	}

	@Transactional
	public void unlikeProduct(Long userId, Long productId) {
		// 상품 존재 여부 확인
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		// 좋아요 정보 조회
		Like productLike = likeRepository.findById(userId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "좋아요 정보를 찾을 수 없습니다."));

		// 상품의 좋아요 수 감소
		LikeCount likeCount = product.getLikeCount().decrease();
		product.updateLikeCount(likeCount);

		// 좋아요 정보 삭제
		likeRepository.delete(productLike);
	}

	@Transactional(readOnly = true)
	public List<LikeInfo> getAllLikedProductIds(Long userId) {
		return likeRepository.getAllLikedByUserId(userId).stream().map(LikeInfo::from).toList();
	}
}
