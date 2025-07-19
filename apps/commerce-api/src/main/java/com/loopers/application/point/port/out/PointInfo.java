package com.loopers.application.point.port.out;

import com.loopers.domain.point.PointEntity;

public record PointInfo(
        Long id,
        Long balance
) {
    public static PointInfo from(final PointEntity model) {
        return new PointInfo(
                model.getId(),
                model.getBalance()
        );
    }
}
