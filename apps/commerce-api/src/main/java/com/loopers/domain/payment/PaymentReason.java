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
public class PaymentReason implements Serializable {

	@Column(name = "reason")
	private String reason;

	public PaymentReason(String reason) {
		if (reason == null || reason.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "결제 사유는 비어있을 수 없습니다.");
		}
		this.reason = reason;
	}

	public String reason() {
		return reason;
	}
}
