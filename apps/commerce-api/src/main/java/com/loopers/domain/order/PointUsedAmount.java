package com.loopers.domain.order;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record PointUsedAmount(Long pointUsedAmount) implements Serializable {

	public PointUsedAmount {
		if (pointUsedAmount == null || pointUsedAmount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트 사용 금액은 0 이상이어야 합니다.");
		}
	}
}
