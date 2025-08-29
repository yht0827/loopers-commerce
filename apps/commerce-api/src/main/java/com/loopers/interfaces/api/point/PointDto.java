package com.loopers.interfaces.api.point;

import java.math.BigDecimal;

import com.loopers.application.point.PointCommand;
import com.loopers.application.point.PointInfo;

public record PointDto() {

	public record V1() {
		public record ChargePointRequest(String userId, BigDecimal balance) {
			public PointCommand.ChargePoint toCommand() {
				return new PointCommand.ChargePoint(userId, balance);
			}
		}

		public record BalanceResponse(Long id, String userId, BigDecimal balance) {
			public static BalanceResponse from(final PointInfo info) {
				return new BalanceResponse(info.id(), info.userId(), info.balance());
			}
		}
	}
}
