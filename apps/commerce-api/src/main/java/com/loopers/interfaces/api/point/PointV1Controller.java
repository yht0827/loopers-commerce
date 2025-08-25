package com.loopers.interfaces.api.point;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.point.facade.PointFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.port.in.PointRequest;
import com.loopers.interfaces.api.point.port.out.ChargeResponse;
import com.loopers.interfaces.api.point.port.out.PointResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {

	private final PointFacade pointFacade;

	@PostMapping("/charge")
	@Override
	public ApiResponse<ChargeResponse> chargePoint(@RequestBody final PointRequest pointRequest) {
		ChargeResponse response = ChargeResponse.from(pointFacade.charge(pointRequest.toCommand()));
		return ApiResponse.success(response);
	}

	@GetMapping
	@Override
	public ApiResponse<PointResponse> getPoint(@RequestHeader("X-USER-ID") final String userId) {
		PointResponse response = PointResponse.from(pointFacade.get(userId));
		return ApiResponse.success(response);
	}
}
