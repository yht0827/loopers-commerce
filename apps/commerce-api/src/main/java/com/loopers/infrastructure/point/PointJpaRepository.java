package com.loopers.infrastructure.point;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.point.Point;

public interface PointJpaRepository extends JpaRepository<Point, Long> {

}
