package com.loopers.application.point.port.out;

import com.loopers.domain.point.PointModel;

public record PointInfo(
        Long id,
        Long balance
) {
    public static PointInfo from(final PointModel model) {
        return new PointInfo(
                model.getId(),
                model.getBalance()
        );
    }
}
