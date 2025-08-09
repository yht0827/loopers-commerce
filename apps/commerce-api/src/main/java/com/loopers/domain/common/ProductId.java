package com.loopers.domain.common;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProductId(Long productId) implements Serializable {
	public ProductId {
		if (productId == null || productId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "제품 ID는 비어있을 수 없습니다.");
		}
	}
}
