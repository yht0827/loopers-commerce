package com.loopers.support;

import com.loopers.config.event.ProductEventType;
import com.loopers.support.ranking.RankingEventType;

public class RankingEventMapper {

	/**
	 * ProductEventType을 RankingEventType으로 변환
	 */
	public static RankingEventType toRankingEventType(ProductEventType productEventType) {
		if (productEventType == null) {
			throw new IllegalArgumentException("ProductEventType cannot be null");
		}

		return switch (productEventType) {
			case VIEW -> RankingEventType.VIEW;
			case LIKE -> RankingEventType.LIKE;
			case ORDER -> RankingEventType.ORDER;
		};
	}

	/**
	 * RankingEventType을 ProductEventType으로 역변환
	 */
	public static ProductEventType toProductEventType(RankingEventType rankingEventType) {
		if (rankingEventType == null) {
			throw new IllegalArgumentException("RankingEventType cannot be null");
		}

		return switch (rankingEventType) {
			case VIEW -> ProductEventType.VIEW;
			case LIKE -> ProductEventType.LIKE;
			case ORDER -> ProductEventType.ORDER;
		};
	}

}
