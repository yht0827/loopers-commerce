package com.loopers.domain.payment;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record PaymentReason(String reason) implements Serializable {

	public PaymentReason {
		if (reason == null || reason.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "결제 사유는 비어있을 수 없습니다.");
		}
	}
}
