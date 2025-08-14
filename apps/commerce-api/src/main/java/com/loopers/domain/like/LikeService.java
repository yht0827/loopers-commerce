package com.loopers.domain.like;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.benmanes.caffeine.cache.Cache;
import com.loopers.domain.product.LikeCount;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

	private static final Duration L2_LIKED_PRODUCTS_TTL = Duration.ofMinutes(15);
	private static final String CACHE_KEY_PREFIX_LIKED_PRODUCTS = "likedProducts:";

	private final LikeRepository likeRepository;
	private final ProductRepository productRepository;
	private final Cache<String, Object> likeL1Cache;
	private final RedisTemplate<String, Object> likeL2Cache;

	@Transactional
	public LikeInfo likeProduct(Long userId, Long productId) {
		// 상품 존재 여부 확인
		Product product = productRepository.findByIdWithPessimisticLock(productId)
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

		// 캐시 무효화
		String cacheKey = CACHE_KEY_PREFIX_LIKED_PRODUCTS + userId;
		likeL1Cache.invalidate(cacheKey);
		likeL2Cache.delete(cacheKey);

		return LikeInfo.from(save);
	}

	@Transactional
	public void unlikeProduct(Long userId, Long productId) {
		// 상품 존재 여부 확인
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		// 좋아요 정보 조회
		Like productLike = likeRepository.findByUserIdAndProductId(userId, productId)
			.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "좋아요 정보를 찾을 수 없습니다."));

		// 상품의 좋아요 수 감소
		LikeCount likeCount = product.getLikeCount().decrease();
		product.updateLikeCount(likeCount);

		// 좋아요 정보 삭제
		likeRepository.delete(productLike);

		// 캐시 무효화
		String cacheKey = CACHE_KEY_PREFIX_LIKED_PRODUCTS + userId;
		likeL1Cache.invalidate(cacheKey);
		likeL2Cache.delete(cacheKey);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<LikeInfo> getAllLikedProductIds(Long userId) {
		String cacheKey = CACHE_KEY_PREFIX_LIKED_PRODUCTS + userId;

		// L1 캐시 조회
		List<LikeInfo> l1Result = (List<LikeInfo>)likeL1Cache.getIfPresent(cacheKey);
		if (l1Result != null) {
			return l1Result;
		}

		// L2 캐시 조회
		List<LikeInfo> l2Result = (List<LikeInfo>)likeL2Cache.opsForValue().get(cacheKey);
		if (l2Result != null) {
			likeL1Cache.put(cacheKey, l2Result);
			return l2Result;
		}

		// DB 조회 및 캐시 저장
		log.debug("Cache miss: {}", cacheKey);
		List<LikeInfo> result = likeRepository.getAllLikedByUserId(userId).stream()
			.map(LikeInfo::from)
			.toList();

		likeL2Cache.opsForValue().set(cacheKey, result, L2_LIKED_PRODUCTS_TTL);
		likeL1Cache.put(cacheKey, result);

		return result;
	}
}
