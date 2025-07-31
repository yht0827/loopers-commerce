package com.loopers.infrastructure.like;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.like.Like;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {
}
