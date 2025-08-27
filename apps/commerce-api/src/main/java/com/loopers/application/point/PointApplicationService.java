package com.loopers.application.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.point.Balance;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointCommandService;
import com.loopers.domain.point.PointQueryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PointApplicationService {
	private final PointCommandService pointCommandService;
	private final PointQueryService pointQueryService;

	public PointInfo chargePoint(final PointCommand.ChargePoint command) {
		Point point = pointQueryService.getPoint(command.userId());

		Balance chargeAmount = Balance.of(command.balance());

		pointCommandService.chargePoint(point, chargeAmount);

		return PointInfo.from(point);
	}

	@Transactional(readOnly = true)
	public PointInfo getPoint(final PointQuery.GetPoint query) {
		Point point = pointQueryService.getPoint(query.userId());
		return PointInfo.from(point);
	}
}
