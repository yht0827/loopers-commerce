package com.loopers.domain.point;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointCommandService {
	private final PointRepository pointRepository;

	public Point chargePoint(final Point sourcePoint) {
		final String userId = sourcePoint.getUserId().userId();

		Point existingPoint = pointRepository.findByUsersId(userId)
			.orElseThrow(() -> new CoreException(NOT_FOUND, POINT_NOT_FOUND.format(userId)));

		existingPoint.charge(sourcePoint.getBalance());

		return pointRepository.save(existingPoint);
	}
}
