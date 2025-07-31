package com.loopers.domain.product.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record QuantityChange(Long quantityChange) implements Serializable {
	public QuantityChange {
		if (quantityChange == null || quantityChange < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "변동 수량은 필수 입력 값입니다.");
		}
	}
}
