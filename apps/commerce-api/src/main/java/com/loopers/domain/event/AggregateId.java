package com.loopers.domain.event;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record AggregateId(String aggregateId) implements Serializable {
	public AggregateId {
		if (aggregateId != null && aggregateId.isBlank()) {
			throw new CoreException(ErrorType.NOT_FOUND, "애그리게이트 ID는 비어있을 수 없습니다.");

		}
	}
}
