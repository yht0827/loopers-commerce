package com.loopers.infrastructure.rank;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.loopers.domain.rank.RankingRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void getTop() {

	}

	@Override
	public void getRankOf() {

	}
}
