package com.loopers.application.ranking;

import java.util.List;

import org.springframework.data.domain.Page;

import com.loopers.domain.rank.RankingItem;

public record RankingPageResult(
	int page, int size, long totalElements, int totalPages,
	List<RankingProductResult> items
) {
	public static RankingPageResult from(Page<RankingItem> pageData, List<RankingProductResult> items) {

		return new RankingPageResult(
			pageData.getNumber(), pageData.getSize(),
			pageData.getTotalElements(), pageData.getTotalPages(),
			items
		);

	}
}

