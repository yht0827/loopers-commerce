package com.loopers.domain.common;

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

	public boolean isSufficient(Quantity amount) {
		return this.quantity >= amount.quantity();
	}

	public Quantity subtract(Quantity amount) {
		if (!isSufficient(amount)) {
			throw new CoreException(ErrorType.BAD_REQUEST, "재고가 충분하지 않습니다.");
		}
		return new Quantity(this.quantity - amount.quantity());
	}

	public Quantity add(Quantity amount) {
		return new Quantity(this.quantity + amount.quantity());
	}
}
