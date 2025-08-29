package com.loopers.interfaces.api.point;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.point.PointApplicationService;
import com.loopers.application.point.PointCommand;
import com.loopers.application.point.PointInfo;
import com.loopers.application.point.PointQuery;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {

	private final PointApplicationService pointApplicationService;

	@PostMapping("/charge")
	@Override
	public ApiResponse<PointDto.V1.BalanceResponse> chargePoint(@RequestBody final PointDto.V1.ChargePointRequest request) {
		PointCommand.ChargePoint command = request.toCommand();
		PointInfo pointInfo = pointApplicationService.chargePoint(command);
		PointDto.V1.BalanceResponse response = PointDto.V1.BalanceResponse.from(pointInfo);
		return ApiResponse.success(response);
	}

	@GetMapping
	@Override
	public ApiResponse<PointDto.V1.BalanceResponse> getPoint(@RequestHeader("X-USER-ID") final String userId) {
		PointQuery.GetPoint query = PointQuery.GetPoint.of(userId);
		PointInfo pointInfo = pointApplicationService.getPoint(query);
		PointDto.V1.BalanceResponse response = PointDto.V1.BalanceResponse.from(pointInfo);
		return ApiResponse.success(response);
	}
}
