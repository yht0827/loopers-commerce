package com.loopers.domain.product.entity.vo;

import com.loopers.support.util.EnumMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StockHistoryType {
	INBOUND("입고"),
	OUTBOUND("출고"),
	ADJUST("재고 조정");

	private final String description;

	// 미리 생성된 매퍼를 static final로 선언
	private static final EnumMapper<StockHistoryType> MAPPER = new EnumMapper<>(StockHistoryType.class);

	public static StockHistoryType from(String type) {
		return MAPPER.from(type, "유효하지 않은 재고 기록 타입입니다.");
	}
}
