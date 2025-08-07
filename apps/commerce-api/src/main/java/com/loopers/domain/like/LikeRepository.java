package com.loopers.domain.like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {

	Like save(Like like);

	void delete(Like like);

	Optional<Like> findById(Long userId); // LikeId로 직접 조회

	List<Like> getAllLikedByUserId(Long userId);

	Optional<Like> findByUserIdAndProductId(Long userId, Long productId);

	Optional<Like> findByUserIdAndProductIdWithPessimisticLock(Long userId, Long productId);

	Optional<Like> findByUserIdAndProductIdWithOptimisticLock(Long userId, Long productId);

}
