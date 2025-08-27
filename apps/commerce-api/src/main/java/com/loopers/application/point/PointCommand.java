package com.loopers.application.point;

import java.math.BigDecimal;

public record PointCommand() {

	public record ChargePoint(String userId, BigDecimal balance) {
	}
}
