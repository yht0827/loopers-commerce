package com.loopers.application.point;

import java.math.BigDecimal;

public record ChargePointCommand(String userId, BigDecimal balance) {
}
