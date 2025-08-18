package com.loopers.interfaces.api.point;

import org.springframework.web.bind.annotation.RequestHeader;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.port.in.PointRequest;
import com.loopers.interfaces.api.point.port.out.ChargeResponse;
import com.loopers.interfaces.api.point.port.out.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Users V1 API", description = "Users API 입니다.")
public interface PointV1ApiSpec {

    @Operation(
            summary = "포인트 충전",
            description = "사용자의 포인트를 충전합니다."
    )
    ApiResponse<ChargeResponse> charge(final PointRequest pointRequest);

    @Operation(
            summary = "포인트 조회",
            description = "사용자의 포인트를 조회합니다."
    )
    ApiResponse<PointResponse> get(final String userId);
}
