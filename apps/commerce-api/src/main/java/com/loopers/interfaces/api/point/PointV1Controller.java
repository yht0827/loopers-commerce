package com.loopers.interfaces.api.point;


import com.loopers.application.point.facade.PointFacade;
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
        ChargeResponse response = ChargeResponse.from(pointFacade.charge(pointRequest.toCommand()));
        return ApiResponse.success(response);
    }

    @GetMapping
    @Override
    public ApiResponse<PointResponse> get(@RequestHeader(value = "X-USER-ID") final Long id) {
        PointResponse response = PointResponse.from(pointFacade.get(id));
        return ApiResponse.success(response);
    }
}
