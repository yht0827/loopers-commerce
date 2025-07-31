package com.loopers.domain.order;

import com.loopers.support.util.EnumMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
	POINT("포인트"),
	CARD("카드"),
	TRANSFER("계좌 이체");

	private final String description;

	// 미리 생성된 매퍼를 static final로 선언
	private static final EnumMapper<PaymentStatus> MAPPER = new EnumMapper<>(PaymentStatus.class);

	public static PaymentStatus from(String type) {
		return MAPPER.from(type, "유효하지 않은 결제 상태 타입입니다.");
	}
}
