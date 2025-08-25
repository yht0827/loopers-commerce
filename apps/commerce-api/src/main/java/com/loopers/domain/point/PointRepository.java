package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {

	Optional<Point> findByUsersId(final String usersId);

	Optional<Point> findByUserIdWithPessimisticLock(final String userId);

	Optional<Point> findByUserIdWithOptimisticLock(final String userId);

	Point save(final Point point);
}
