package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {

    Optional<PointModel> findByUsersId(final Long usersId);

    PointModel save(final PointModel pointModel);
}
