package com.loopers.support.util;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class EnumMapper<E extends Enum<E>> {

	private final Map<String, E> map;

	public EnumMapper(Class<E> enumClass) {
		// Enum의 이름(name)을 키로 사용하는 Map을 미리 생성
		this.map = Arrays.stream(enumClass.getEnumConstants())
			.collect(Collectors.toMap(e -> e.name().toUpperCase(), Function.identity()));
	}

	/**
	 * 문자열 키를 기반으로 Enum 상수를 반환합니다.
	 * @param key 조회할 문자열 (대소문자 구분 없음)
	 * @param errorMessage 키가 존재하지 않을 경우 반환할 예외 메시지
	 * @return E Enum 상수
	 */
	public E from(String key, String errorMessage) {
		if (key == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, errorMessage);
		}

		E result = map.get(key.toUpperCase());
		if (result == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, errorMessage);
		}
		return result;
	}
}
