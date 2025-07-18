package com.loopers.interfaces.api.point.port.out;

import com.loopers.application.point.port.out.PointInfo;

public record ChargeResponse(
        Long id,
        Long balance
) {
    public static ChargeResponse from(final PointInfo info) {
        return new ChargeResponse(
                info.id(),
                info.balance()
        );
    }
}
