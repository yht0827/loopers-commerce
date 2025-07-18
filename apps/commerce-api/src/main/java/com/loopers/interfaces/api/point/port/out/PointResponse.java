package com.loopers.interfaces.api.point.port.out;

import com.loopers.application.point.port.out.PointInfo;

public record PointResponse(
        Long id,
        Long balance
) {
    public static PointResponse from(final PointInfo info) {
        return new PointResponse(
                info.id(),
                info.balance()
        );
    }
}
