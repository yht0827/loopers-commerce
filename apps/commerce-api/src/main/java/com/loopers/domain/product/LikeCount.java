package com.loopers.domain.product;

import java.io.Serializable;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeCount implements Serializable {

	@Column(name = "like_count")
	private Long likeCount;

	public LikeCount(Long likeCount) {
		if (likeCount == null || likeCount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 수는 0 이상이어야 합니다.");
		}
		this.likeCount = likeCount;
	}

	public static LikeCount Zero() {
		return new LikeCount(0L);
	}
}
