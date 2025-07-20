package com.loopers.interfaces.api.point;


import com.loopers.application.point.PointFacade;
import com.loopers.application.point.port.out.PointInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.port.in.PointRequest;
import com.loopers.interfaces.api.point.port.out.ChargeResponse;
import com.loopers.interfaces.api.point.port.out.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/point")
public class PointV1Controller implements PointV1ApiSpec {

    private final PointFacade pointFacade;

    @PostMapping("/charge")
    @Override
    public ApiResponse<ChargeResponse> charge(@RequestBody final PointRequest pointRequest) {
        PointInfo pointInfo = pointFacade.charge(pointRequest.toCommand());
        ChargeResponse response = ChargeResponse.from(pointInfo);
        return ApiResponse.success(response);
    }

    @GetMapping
    @Override
    public ApiResponse<PointResponse> getPointById(@RequestHeader("X-USER-ID") final Long id) {
        PointInfo pointInfo = pointFacade.get(id);
        PointResponse response = PointResponse.from(pointInfo);
        return ApiResponse.success(response);
    }
}
