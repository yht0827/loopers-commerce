package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {

	Optional<Point> findByUsersId(final Long usersId);

	Optional<Point> findByUserIdWithPessimisticLock(Long userId);

	Optional<Point> findByUserIdWithOptimisticLock(Long userId);

	Point save(final Point point);
}
