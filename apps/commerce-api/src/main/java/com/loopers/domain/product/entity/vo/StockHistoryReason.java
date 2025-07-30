package com.loopers.domain.product.entity.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record StockHistoryReason(String reason) {
	public StockHistoryReason {
		if (reason == null || reason.isBlank()) {
			throw new IllegalArgumentException("재고 이력 사유는 필수 입력 값입니다.");
		}
	}
}
