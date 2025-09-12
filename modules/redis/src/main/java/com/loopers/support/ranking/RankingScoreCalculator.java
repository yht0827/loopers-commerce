package com.loopers.support.ranking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RankingScoreCalculator {

	private static final double VIEW_WEIGHT = 0.1;
	private static final double LIKE_WEIGHT = 0.2;
	private static final double ORDER_WEIGHT = 0.6;

	public double calculateScore(WeightType type, Map<String, Object> eventData) {
		return switch (type) {
			case VIEW -> VIEW_WEIGHT;
			case LIKE -> LIKE_WEIGHT;
			case ORDER -> ORDER_WEIGHT;
		};
	}

	/**
	 * 시간 가중치 적용 (최근 이벤트에 더 높은 점수)
	 */
	public double applyTimeDecay(double baseScore, LocalDateTime eventTime) {
		long hoursAgo = ChronoUnit.HOURS.between(eventTime, LocalDateTime.now());
		double decayFactor = Math.exp(-0.1 * hoursAgo);
		return baseScore * decayFactor;
	}

}
