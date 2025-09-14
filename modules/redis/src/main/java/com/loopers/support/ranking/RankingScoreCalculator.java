package com.loopers.support.ranking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class RankingScoreCalculator {

	private static final double VIEW_WEIGHT = 0.1;
	private static final double LIKE_WEIGHT = 0.2;
	private static final double ORDER_WEIGHT = 0.6;

	public double calculateScore(RankingEventType type) {
		return switch (type) {
			case VIEW -> VIEW_WEIGHT;
			case LIKE -> LIKE_WEIGHT;
			case ORDER -> ORDER_WEIGHT;
		};
	}

	public double applyTimeDecay(double baseScore, LocalDateTime eventTime) {
		long hoursAgo = ChronoUnit.HOURS.between(eventTime, LocalDateTime.now());
		double decayFactor = Math.exp(-0.1 * hoursAgo);
		return baseScore * decayFactor;
	}

}
