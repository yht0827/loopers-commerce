package com.loopers.domain.event;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record EventId(String eventId) implements Serializable {
	public EventId {
		if (eventId == null || eventId.isBlank()) {
			throw new CoreException(ErrorType.NOT_FOUND, "이벤트 ID는 비어있을 수 없습니다.");
		}
	}
}
