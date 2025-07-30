package com.loopers.domain.like.entity.vo;

import java.util.Arrays;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetType {
	PRODUCT("상품"),
	BRAND("브랜드");

	private final String description;

	public static TargetType from(String targetType) {
		return Arrays.stream(values())
			.filter(type -> type.name().equalsIgnoreCase(targetType))
			.findFirst()
			.orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "유효하지 좋아요 타입입니다."));
	}
}
