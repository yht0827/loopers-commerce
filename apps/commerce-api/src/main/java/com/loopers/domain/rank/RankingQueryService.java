package com.loopers.domain.rank;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankingQueryService {

	private final RankingRepository repository;
	private final RankingValidator validator;

	public void getRanking(final String date, final Pageable pageable) {



	}
}
