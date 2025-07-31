package com.loopers.domain.product.entity.vo;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record SkuOptionValueId(Long skuId, Long optionValueId) implements Serializable {
	public SkuOptionValueId {
		if (skuId == null || skuId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "SKU ID는 는 비어있을 수 없습니다.");
		}
		if (optionValueId == null || optionValueId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "옵션 값 ID는 비어있을 수 없습니다.");
		}
	}
}
