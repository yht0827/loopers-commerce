package com.loopers.domain.rank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankingItem {

	private Long rank;
	private Long productId;
	private Double score;

	@Builder(builderMethodName = "create")
	public RankingItem(Long rank, Long productId, Double score) {
		this.rank = rank;
		this.productId = productId;
		this.score = score;
	}
}
