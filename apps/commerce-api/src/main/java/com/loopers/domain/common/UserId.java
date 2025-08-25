package com.loopers.domain.common;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(String userId) implements Serializable {

	public UserId {
		if (userId == null || userId.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "회원 ID는 비어있을 수 없습니다.");
		}
	}
}
