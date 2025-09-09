package com.loopers.application.ranking;

import org.springframework.data.domain.Pageable;

public record GetRankingQuery(
	String userId,
	String date,
	Pageable pageable
) {
}
