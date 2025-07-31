package com.loopers.domain.like;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record LikeId(Long userId, Long targetId) {
	public LikeId {
		if (userId == null || userId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "사용자 ID는 비어있을 수 없습니다.");
		}
		if (targetId == null || targetId <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 대상 ID는 비어있을 수 없습니다.");
		}
	}
}
