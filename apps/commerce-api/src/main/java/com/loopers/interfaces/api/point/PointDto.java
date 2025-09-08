package com.loopers.interfaces.api.point;

import java.math.BigDecimal;

import com.loopers.application.point.ChargePointCommand;
import com.loopers.application.point.PointResult;

public record PointDto() {

	public record V1() {
		public record ChargePointRequest(String userId, BigDecimal balance) {
			public ChargePointCommand toCommand() {
				return new ChargePointCommand(userId, balance);
			}
		}

		public record BalanceResponse(Long id, String userId, BigDecimal balance) {
			public static BalanceResponse from(final PointResult info) {
				return new BalanceResponse(info.id(), info.userId(), info.balance());
			}
		}
	}
}
