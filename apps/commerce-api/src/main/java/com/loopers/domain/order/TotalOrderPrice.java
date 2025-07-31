package com.loopers.domain.order;

import java.io.Serializable;
import java.math.BigDecimal;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record TotalOrderPrice(BigDecimal totalPrice) implements Serializable {
	public TotalOrderPrice {
		if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "총 주문 금액은 0 이상이어야 합니다.");
		}
	}
}


