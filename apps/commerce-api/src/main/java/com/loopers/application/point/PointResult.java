package com.loopers.application.point;

import java.math.BigDecimal;

import com.loopers.domain.point.Point;

public record PointResult(
	Long id,
	String userId,
	BigDecimal balance
) {
	public static PointResult from(final Point point) {
		return new PointResult(
			point.getId(),
			point.getUserId().getUserId(),
			point.getBalance().getBalance()
		);
	}
}
