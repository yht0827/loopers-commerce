package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Optional<PointEntity> findByUsersId(final Long id) {
        return pointJpaRepository.findById(id);
    }

    @Override
    public PointEntity save(final PointEntity pointEntity) {
        return pointJpaRepository.save(pointEntity);
    }
}
