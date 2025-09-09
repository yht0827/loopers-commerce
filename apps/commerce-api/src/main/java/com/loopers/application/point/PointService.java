package com.loopers.application.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.point.Balance;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointDomainService;
import com.loopers.domain.point.PointFinder;
import com.loopers.domain.user.UserId;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService implements PointUseCase {
	private final PointDomainService pointDomainService;
	private final PointFinder pointFinder;

	@Override
	public PointResult chargePoint(final ChargePointCommand command) {

		Point point = pointDomainService.charge(
			UserId.of(command.userId()),
			Balance.of(command.balance())
		);

		return PointResult.from(point);
	}

	@Override
	@Transactional(readOnly = true)
	public PointResult getPoint(final GetPointQuery query) {
		final UserId userId = UserId.of(query.userId());

		Point point = pointFinder.getPoint(userId);
		return PointResult.from(point);
	}
}
