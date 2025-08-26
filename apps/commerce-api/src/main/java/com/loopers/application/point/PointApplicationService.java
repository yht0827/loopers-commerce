package com.loopers.application.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointCommandService;
import com.loopers.domain.point.PointQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointApplicationService {
	private final PointCommandService pointCommandService;
	private final PointQueryService pointQueryService;

	@Transactional
	public PointInfo chargePoint(final PointCommand.ChargePoint command) {
		final Point point = command.toEntity();
		Point savedPoint = pointCommandService.chargePoint(point);
		return PointInfo.from(savedPoint);
	}

	@Transactional(readOnly = true)
	public PointInfo getPoint(final PointQuery.GetPoint query) {
		Point point = pointQueryService.getPoint(query.userId());
		return PointInfo.from(point);
	}
}
