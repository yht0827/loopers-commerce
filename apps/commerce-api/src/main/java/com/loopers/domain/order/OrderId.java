package com.loopers.domain.order;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record OrderId(Long orderId) implements Serializable {
	public OrderId {
		if (orderId == null || orderId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 ID는 비어있을 수 없습니다.");
		}
	}
}
