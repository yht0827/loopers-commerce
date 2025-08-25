package com.loopers.application.point.port.in;

public record PointCommand(
        String userId,
        Long balance
) {
}
