package com.loopers.domain.common;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record BrandId(Long brandId) implements Serializable {
	public BrandId {
		if (brandId == null || brandId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "브랜드 ID는 비어있을 수 없습니다.");
		}
	}
}
