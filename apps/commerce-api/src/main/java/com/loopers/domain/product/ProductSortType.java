package com.loopers.domain.product;

import java.util.Arrays;

import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductSortType {
	LATEST("latest", Sort.by("createdAt").descending()), // 생성일시 기준
	PRICE_ASC("price_asc", Sort.by("price").ascending()),     // 대표 가격 기준
	LIKES_DESC("likes_desc", Sort.by("likesCount").descending()); // 좋아요 수 기준

	private final String requestValue;
	private final Sort sort;

	public static ProductSortType from(String type) {
		if (type == null) {
			return LATEST;
		}

		return Arrays.stream(values())
			.filter(it -> it.requestValue.equalsIgnoreCase(type))
			.findFirst()
			.orElse(LATEST); // 잘못된 값이 들어올 경우 기본값 처리
	}
}
