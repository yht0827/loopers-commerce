package com.loopers.application.point.port.out;

import com.loopers.domain.point.Point;

public record PointInfo(
        Long id,
        Long balance
) {
    public static PointInfo from(final Point model) {
        return new PointInfo(
                model.getId(),
                model.getBalance()
        );
    }
}
