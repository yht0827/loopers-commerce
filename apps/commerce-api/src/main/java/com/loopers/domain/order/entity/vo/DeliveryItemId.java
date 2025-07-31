package com.loopers.domain.order.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record DeliveryItemId(Long deliveryId, Long orderItemId) implements Serializable {
	public DeliveryItemId {
		if (deliveryId == null || deliveryId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "배송 ID는 비어있을 수 없습니다.");
		}
		if (orderItemId == null || orderItemId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 상품 항목 ID는 비어있을 수 없습니다.");
		}
	}
}
