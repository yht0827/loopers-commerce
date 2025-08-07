package com.loopers.infrastructure.like;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
	private final LikeJpaRepository likeJpaRepository;

	@Override
	public Like save(Like like) {
		return likeJpaRepository.save(like);
	}

	@Override
	public void delete(Like like) {
		likeJpaRepository.delete(like);
	}

	@Override
	public Optional<Like> findById(Long userId) {
		return likeJpaRepository.findById(userId);
	}

	@Override
	public List<Like> getAllLikedByUserId(Long userId) {
		return likeJpaRepository.findAllByUserId(userId);
	}

	@Override
	public Optional<Like> findByUserIdAndProductId(Long userId, Long productId) {
		return likeJpaRepository.findByUserIdAndProductId(userId, productId);
	}

	@Override
	public Optional<Like> findByUserIdAndProductIdWithPessimisticLock(Long userId, Long productId) {
		return likeJpaRepository.findByUserIdAndProductIdWithPessimisticLock(userId, productId);
	}

	@Override
	public Optional<Like> findByUserIdAndProductIdWithOptimisticLock(Long userId, Long productId) {
		return likeJpaRepository.findByUserIdAndProductIdWithOptimisticLock(userId, productId);
	}

}
