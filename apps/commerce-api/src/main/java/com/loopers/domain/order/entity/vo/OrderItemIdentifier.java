package com.loopers.domain.order.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderItemIdentifier(OrderId orderId, Long skuId, String orderNumber) implements Serializable {
	public OrderItemIdentifier {
		if (skuId == null || skuId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "SKU ID는 비어있을 수 없습니다.");
		}
		if (orderNumber == null || orderNumber.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 번호는 비어있을 수 없습니다.");
		}
	}
}
