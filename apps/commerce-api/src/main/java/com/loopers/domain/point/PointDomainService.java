package com.loopers.domain.point;

import static com.loopers.support.error.ErrorMessage.*;
import static com.loopers.support.error.ErrorType.*;

import org.springframework.stereotype.Component;

import com.loopers.domain.user.UserId;
import com.loopers.support.error.CoreException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointDomainService {
	private final PointRepository pointRepository;

	public Point charge(final UserId userId, final Balance amount) {

		Point point = pointRepository.findByUsersId(userId)
			.orElseThrow(() -> new CoreException(NOT_FOUND, POINT_NOT_FOUND.format(userId)));

		point.chargeBalance(amount);

		return point;
	}

}
