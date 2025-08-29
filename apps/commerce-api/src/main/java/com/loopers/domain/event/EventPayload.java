package com.loopers.domain.event;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EventPayload(
	@Column(columnDefinition = "json")
	String payload
) implements Serializable {
	public EventPayload {
		if (payload != null && payload.isBlank()) {
			throw new CoreException(ErrorType.NOT_FOUND, "이벤트 페이로드는 비어있을 수 없습니다.");
		}
	}
}
