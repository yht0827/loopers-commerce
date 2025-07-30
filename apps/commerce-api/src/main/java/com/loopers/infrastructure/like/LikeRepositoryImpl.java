package com.loopers.infrastructure.like;

import org.springframework.stereotype.Repository;

import com.loopers.domain.like.LikeRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
	private final LikeJpaRepository likeJpaRepository;
}
