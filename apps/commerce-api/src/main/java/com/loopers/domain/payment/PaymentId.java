package com.loopers.domain.payment;

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
public class PaymentId implements Serializable {
	
	@Column(name = "payment_id")
	private Long paymentId;
	
	public PaymentId(Long paymentId) {
		if (paymentId == null || paymentId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "결제 ID는 비어있을 수 없습니다.");
		}
		this.paymentId = paymentId;
	}
}
