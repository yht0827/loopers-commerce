package com.loopers.domain.order;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record Quantity(Long quantity) implements Serializable {
	public Quantity {
		if (quantity == null || quantity < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "수량은 0 이상이어야 합니다.");
		}
	}
}
