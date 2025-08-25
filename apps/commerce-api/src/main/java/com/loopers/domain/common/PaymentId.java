package com.loopers.domain.common;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record PaymentId(Long paymentId) implements Serializable {

	public PaymentId {
		if (paymentId == null || paymentId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "결제 ID는 비어있을 수 없습니다.");
		}
	}
}
