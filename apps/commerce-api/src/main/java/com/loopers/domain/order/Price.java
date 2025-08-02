package com.loopers.domain.order;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record Price(Long price) implements Serializable {
	public Price {
		if (price == null || price < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "가격은 0 이상이어야 합니다.");
		}
	}
}
