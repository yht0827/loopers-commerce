package com.loopers.domain.product.entity.vo;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record OptionSpec(String value) {
	public OptionSpec {
		if (value == null || value.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "옵션 값은 비어있을 수 없습니다.");
		}
	}
}
