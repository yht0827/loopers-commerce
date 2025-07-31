package com.loopers.domain.product.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record SkuCode(String skuCode) implements Serializable {
	public SkuCode {
		if (skuCode == null || skuCode.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "SKU 코드는 비어있을 수 없습니다.");
		}
	}
}
