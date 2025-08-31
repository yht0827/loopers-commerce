package com.loopers.domain.point;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointCommandService {

	public void chargePoint(final Point point, final Balance chargeAmount) {
		point.chargeBalance(chargeAmount);
	}
}
