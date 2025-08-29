package com.loopers.domain.point;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointQueryService {
	private final PointRepository pointRepository;

	@Transactional(readOnly = true)
	public Point getPoint(final String usersId) {
		return pointRepository.findByUsersId(usersId)
			.orElseThrow(() -> new CoreException(NOT_FOUND, POINT_NOT_FOUND.format(usersId)));
	}
}
