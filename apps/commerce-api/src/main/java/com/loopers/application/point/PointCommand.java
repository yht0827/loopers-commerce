package com.loopers.application.point;

import java.math.BigDecimal;

import com.loopers.domain.common.UserId;
import com.loopers.domain.point.Balance;
import com.loopers.domain.point.Point;

public record PointCommand() {

	public record ChargePoint(
		String userId,
		BigDecimal balance
	) {
		public Point toEntity() {
			return Point.builder()
				.userId(new UserId(userId))
				.balance(new Balance(balance))
				.build();
		}
	}
}
