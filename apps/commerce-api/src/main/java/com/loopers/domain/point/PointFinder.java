package com.loopers.domain.point;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.user.UserId;
import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointFinder {
	private final PointRepository pointRepository;

	@Transactional(readOnly = true)
	public Point getPoint(final UserId userId) {
		return pointRepository.findByUsersId(userId)
			.orElseThrow(() -> new CoreException(NOT_FOUND, POINT_NOT_FOUND.format(userId)));
	}
}
