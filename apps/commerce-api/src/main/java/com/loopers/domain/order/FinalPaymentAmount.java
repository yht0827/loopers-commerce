package com.loopers.domain.order;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record FinalPaymentAmount(Long finalPaymentAmount) implements Serializable {

	public FinalPaymentAmount {
		if (finalPaymentAmount == null || finalPaymentAmount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "최종 결제 금액은 0 이상이어야 합니다.");
		}
	}

	public static FinalPaymentAmount of(final Long totalOrderPrice, final Long couponDiscountAmount) {

		return new FinalPaymentAmount(totalOrderPrice - couponDiscountAmount);
	}

}
