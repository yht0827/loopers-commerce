package com.loopers.domain.event;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record CorrelationId(String correlationId) implements Serializable {
	public CorrelationId {
		if (correlationId != null && correlationId.isBlank()) {
			throw new CoreException(ErrorType.NOT_FOUND, "Correlation ID는 비어있을 수 없습니다.");
		}
	}
}
