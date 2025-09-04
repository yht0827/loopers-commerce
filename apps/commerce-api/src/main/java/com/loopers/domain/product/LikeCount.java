package com.loopers.domain.product;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Embeddable;

@Embeddable
public record LikeCount(Long likeCount) implements Serializable {

	public LikeCount {
		if (likeCount == null || likeCount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 수는 0 이상이어야 합니다.");
		}
	}

	public static LikeCount Zero() {
		return new LikeCount(0L);
	}
}
