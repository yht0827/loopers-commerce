package com.loopers.application.point.port.in;

public record PointCommand(
        Long userId,
        Long balance
) {
}
