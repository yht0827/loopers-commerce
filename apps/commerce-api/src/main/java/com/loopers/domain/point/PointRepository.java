package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {

    Optional<Point> findByUsersId(final Long usersId);

    Point save(final Point point);
}
