package com.loopers.domain.product.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProductName(String name) implements Serializable {
	public ProductName {
		if (name == null || name.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품 이름은 비어있을 수 없습니다.");
		}
	}
}
