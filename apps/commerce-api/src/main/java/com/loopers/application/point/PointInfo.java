package com.loopers.application.point;

import java.math.BigDecimal;

import com.loopers.domain.point.Point;

public record PointInfo(
	Long id,
	String userId,
	BigDecimal balance
) {
	public static PointInfo from(final Point point) {
		return new PointInfo(
			point.getId(),
			point.getUserId().getUserId(),
			point.getBalance().getBalance()
		);
	}
}
