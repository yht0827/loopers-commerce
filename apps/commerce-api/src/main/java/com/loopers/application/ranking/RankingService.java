package com.loopers.application.ranking;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.rank.RankingQueryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RankingService implements RankingUseCase {

	private final RankingQueryService queryService;

	@Override
	public RankingResult getRanking(final GetRankingQuery query) {

		queryService.getRanking(query.date(), query.pageable());

		return null;
	}
}
