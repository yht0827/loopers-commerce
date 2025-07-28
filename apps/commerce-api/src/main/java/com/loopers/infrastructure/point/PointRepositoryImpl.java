package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Optional<PointModel> findByUsersId(final Long id) {
        return pointJpaRepository.findById(id);
    }

    @Override
    public PointModel save(final PointModel pointModel) {
        return pointJpaRepository.save(pointModel);
    }
}
