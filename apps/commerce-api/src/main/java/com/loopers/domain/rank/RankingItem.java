package com.loopers.domain.rank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankingItem {

	private Long rank;
	private String productId;
	private Long score;

	@Builder(builderMethodName = "create")
	public RankingItem(Long rank, String productId, Long score) {
		this.rank = rank;
		this.productId = productId;
		this.score = score;
	}
}
