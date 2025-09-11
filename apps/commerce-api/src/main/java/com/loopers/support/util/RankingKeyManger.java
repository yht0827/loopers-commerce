package com.loopers.support.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class RankingKeyManger {

	private static final String KEY_PREFIX = "ranking";
	private static final String DATE_FORMAT = "yyyyMMdd";
	private static final String HOUR_FORMAT = "yyyyMMddHH";
	private static final long DAILY_TTL = 172800L;
	private static final long HOURLY_TTL = 90000L;

	private final DateTimeFormatter dailyFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

	public String getDailyRankingKey(final LocalDate date) {
		return String.format("%s:daily:all:%s", KEY_PREFIX, date.format(dailyFormatter));
	}

	public String getDailyCategoryRankingKey(final LocalDate date, String category) {
		return String.format("%s:daily:%s:%s", KEY_PREFIX, category, date.format(dailyFormatter));
	}

	public String getHourlyRankingKey(final LocalDate date) {
		return String.format("%s:hourly:%s", KEY_PREFIX, date.format(dailyFormatter));
	}

	public long getTTL(final RankingType type) {
		return switch (type) {
			case DAILY -> DAILY_TTL;
			case HOURLY -> HOURLY_TTL;
		};
	}

	public enum RankingType {
		DAILY, HOURLY
	}
}
