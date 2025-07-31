package com.loopers.domain.order.entity.vo;

import com.loopers.domain.like.entity.vo.TargetType;
import com.loopers.support.util.EnumMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

	PENDING("주문 대기 중"),
	CONFIRMED("주문 확인됨"),
	SHIPPED("배송 중"),
	DELIVERED("배송 완료"),
	CANCELLED("주문 취소됨");

	private final String description;

	// 미리 생성된 매퍼를 static final로 선언
	private static final EnumMapper<TargetType> MAPPER = new EnumMapper<>(TargetType.class);

	public static TargetType from(String type) {
		return MAPPER.from(type, "유효하지 않은 주문 상태 타입입니다.");
	}

}
