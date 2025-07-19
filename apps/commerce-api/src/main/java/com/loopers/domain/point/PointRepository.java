package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {

    Optional<PointEntity> findByUsersId(final Long usersId);

    PointEntity save(final PointEntity pointEntity);
}
