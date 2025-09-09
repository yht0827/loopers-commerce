package com.loopers.interfaces.api.rank;

import org.springframework.data.domain.PageRequest;

import com.loopers.application.ranking.GetRankingQuery;
import com.loopers.application.ranking.RankingResult;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record RankingDto() {

	public record V1() {

		public record RankingRequest(
			String date,
			@PositiveOrZero Integer page,
			@Positive Integer size
		) {
			public GetRankingQuery from(final String userId) {
				return new GetRankingQuery(userId, this.date,
					PageRequest.of(
						page != null ? page : 0,
						size != null ? size : 10
					));
			}
		}

		public record RankingResponse(Integer top) {
			public static RankingDto.V1.RankingResponse from(final RankingResult rankingResult) {
				return new RankingDto.V1.RankingResponse(0);
			}
		}
	}
}
