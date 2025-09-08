package com.loopers.domain.point;

import java.util.Optional;

import com.loopers.domain.user.UserId;

public interface PointRepository {

	Optional<Point> findByUsersId(final UserId usersId);

	Optional<Point> findByUserIdWithPessimisticLock(final UserId userId);

	Optional<Point> findByUserIdWithOptimisticLock(final UserId userId);

	Point save(final Point point);
}
