package com.loopers.application.point;

public interface PointUseCase {

	PointResult chargePoint(final ChargePointCommand command);

	PointResult getPoint(final GetPointQuery query);
}
