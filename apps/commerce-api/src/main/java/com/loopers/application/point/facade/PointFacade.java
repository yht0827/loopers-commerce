package com.loopers.application.point.facade;

import org.springframework.stereotype.Component;

import com.loopers.application.point.port.in.PointCommand;
import com.loopers.application.point.port.out.PointInfo;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PointFacade {

	private final PointService pointService;

	public PointInfo charge(final PointCommand command) {
		Point point = pointService.charge(command);
		return PointInfo.from(point);
	}

	public PointInfo get(final String id) {
		Point point = pointService.getPoint(id);
		return PointInfo.from(point);
	}
}
