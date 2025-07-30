package com.loopers.domain.product.entity.vo;

import java.math.BigDecimal;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record Price(BigDecimal price) {
	public Price {
		if (price == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "가격은 필수 입력 값입니다.");
		}

		if (price.signum() < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "가격은 0 이상이어야 합니다.");
		}
	}
}
