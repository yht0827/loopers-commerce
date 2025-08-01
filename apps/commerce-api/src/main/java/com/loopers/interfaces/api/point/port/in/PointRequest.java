package com.loopers.interfaces.api.point.port.in;

import com.loopers.application.point.port.in.PointCommand;

public record PointRequest(
        Long userId,
        Long balance
) {
    public PointCommand toCommand() {
        return new PointCommand(userId, balance);
    }
}
