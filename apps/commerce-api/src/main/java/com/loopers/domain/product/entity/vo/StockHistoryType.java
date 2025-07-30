package com.loopers.domain.product.entity.vo;

import java.util.Arrays;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StockHistoryType {
	INBOUND("입고"),
	OUTBOUND("출고"),
	ADJUST("재고 조정");

	private final String description;

	public static StockHistoryType from(String type) {
		return Arrays.stream(values())
			.filter(it -> it.name().equalsIgnoreCase(type))
			.findFirst()
			.orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "유효하지 않은 재고 기록 타입입니다."));
	}

}
