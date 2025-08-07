package com.loopers.infrastructure.point;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepository {

	private final PointJpaRepository pointJpaRepository;

	@Override
	public Optional<Point> findByUsersId(final Long id) {
		return pointJpaRepository.findByUserId(id);
	}

	@Override
	public Optional<Point> findByUserIdWithPessimisticLock(Long userId) {
		return pointJpaRepository.findByUserIdWithPessimisticLock(userId);
	}

	@Override
	public Optional<Point> findByUserIdWithOptimisticLock(Long userId) {
		return pointJpaRepository.findByUserIdWithOptimisticLock(userId);
	}

	@Override
	public Point save(final Point point) {
		return pointJpaRepository.save(point);
	}
}
