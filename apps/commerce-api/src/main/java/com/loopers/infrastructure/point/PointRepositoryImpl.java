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
	public Optional<Point> findByUsersId(final String userId) {
		return pointJpaRepository.findByUserId(userId);
	}

	@Override
	public Optional<Point> findByUserIdWithPessimisticLock(final String userId) {
		return pointJpaRepository.findByUserIdWithPessimisticLock(userId);
	}

	@Override
	public Optional<Point> findByUserIdWithOptimisticLock(final String userId) {
		return pointJpaRepository.findByUserIdWithOptimisticLock(userId);
	}

	@Override
	public Point save(final Point point) {
		return pointJpaRepository.save(point);
	}
}
