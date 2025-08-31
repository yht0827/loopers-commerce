package com.loopers.domain.event;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record EventType(String eventType) implements Serializable {
	public EventType {
		if (eventType == null || eventType.isBlank()) {
			throw new CoreException(ErrorType.NOT_FOUND, "이벤트 타입은 비어있을 수 없습니다.");
		}
	}
}
