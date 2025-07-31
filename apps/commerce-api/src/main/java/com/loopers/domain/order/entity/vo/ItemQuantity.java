package com.loopers.domain.order.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record ItemQuantity(Long quantity) implements Serializable {
	public ItemQuantity {
		if (quantity == null || quantity <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "수량은 0보다 커야 합니다.");
		}
	}
}
