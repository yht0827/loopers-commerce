package com.loopers.domain.order;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FinalPaymentAmount implements Serializable {

	@Column(name = "final_payment_amount")
	private Long finalPaymentAmount;

	public FinalPaymentAmount(Long finalPaymentAmount) {
		if (finalPaymentAmount == null || finalPaymentAmount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "최종 결제 금액은 0 이상이어야 합니다.");
		}
		this.finalPaymentAmount = finalPaymentAmount;
	}

	public static FinalPaymentAmount of(final Long totalOrderPrice, final Long couponDiscountAmount) {
		return new FinalPaymentAmount(totalOrderPrice - couponDiscountAmount);
	}

}
