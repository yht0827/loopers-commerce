package com.loopers.domain.order.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderId(Long orderId) implements Serializable {
	public OrderId {
		if (orderId == null || orderId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 ID는 비어있을 수 없습니다.");
		}
	}
}
