package com.loopers.domain.order.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderItemDetail(Long quantity, BigDecimal price) implements Serializable {
	public OrderItemDetail {
		if (quantity == null || quantity <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "수량은 0보다 커야 합니다.");
		}
		if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "가격은 0 이상이어야 합니다.");
		}
	}
}
