package com.loopers.domain.order;

import java.io.Serializable;
import java.math.BigDecimal;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record PaymentDetail(BigDecimal amount, String method) implements Serializable {
	public PaymentDetail {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "결제 금액은 0 이상이어야 합니다.");
		}

		if (method == null || method.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "결제 수단은 비어있을 수 없습니다.");
		}
	}
}
